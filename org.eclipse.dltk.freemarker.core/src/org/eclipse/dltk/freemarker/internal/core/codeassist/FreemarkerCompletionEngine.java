/*******************************************************************************
 * Copyright (c) 2010 Freemarker Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.freemarker.internal.core.codeassist;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.codeassist.ScriptCompletionEngine;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.IAccessRule;
import org.eclipse.dltk.freemarker.core.FreemarkerCorePlugin;
import org.eclipse.dltk.freemarker.core.util.SettingsUtils;

import freemarker.provisionnal.ext.ide.AssistUtils;
import freemarker.provisionnal.ext.ide.PositionCalculator;
import freemarker.provisionnal.ext.ide.TemplateEntriesModel;
import freemarker.provisionnal.ext.ide.TemplateModelCollector;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Freemarker completion engine.
 * 
 * @author Angelo ZERR
 * 
 */
public class FreemarkerCompletionEngine extends ScriptCompletionEngine
		implements TemplateModelCollector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.codeassist.ICompletionEngine#complete(org.eclipse.dltk
	 * .compiler.env.IModuleSource, int, int)
	 */
	public void complete(IModuleSource module, int position, int i) {
		this.requestor.beginReporting();

		// Get the template content
		String content = module.getSourceContents();
		if (position < 0 || position > content.length()) {
			return;
		}

		// Get the template file linked to the module
		IFile templateFile = getTemplateFile(module);

		// Get Freemarker tag syntax
		Configuration configuration = getConfiguration(templateFile);
		int tagSyntax = (configuration != null ? configuration.getTagSyntax()
				: Configuration.AUTO_DETECT_TAG_SYNTAX);

		// get position calculator to retrieve keyword used for autocmpletion
		// and know the token type (If directive, Interpolation...)
		// where cursor is positioned
		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax);
		if (calculator != null && calculator.getCompletion() != null) {

			switch (calculator.getTokenType()) {
			case EXPRESSION:
				// Cursor is positioned to an expression (Interpolation, If,
				// list directive...)
				String expression = calculator.getCompletion();

				// Get paths from the expression (split expression with '.')
				String[] paths = AssistUtils.getExpressionPaths(expression);

				// Update DLTK source range
				if (paths.length < 1) {
					this.setSourceRange(position - expression.length(),
							position);
				} else {
					this.setSourceRange(
							position - paths[paths.length - 1].length(),
							position);
				}

				// Get TemplateEntriesModel to search
				TemplateEntriesModel entriesModel = getTemplateEntriesModel(templateFile);
				if (entriesModel != null) {
					reportDataModel(entriesModel, paths);
				}
				break;
			}
		}

		this.requestor.endReporting();
	}

	/**
	 * Collect Data-Model which match paths
	 * 
	 * @param entriesModel
	 * @param paths
	 */
	private void reportDataModel(TemplateEntriesModel entriesModel,
			String[] paths) {
		try {
			// Collect Data-Model which match paths.
			entriesModel.findDataModel(paths, this);
		} catch (TemplateModelException e) {
			FreemarkerCorePlugin.log(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemarker.provisionnal.template.TemplateModelCollector#collect(freemarker
	 * .template.TemplateModel, freemarker.template.TemplateModel,
	 * java.lang.String[])
	 */
	public void collect(TemplateModel modelKey, TemplateModel modelValue,
			String paths[]) {

		// Get DLTK kind (variable, method...) for item completion proposal
		int kind = (paths.length <= 1 ? CompletionProposal.LOCAL_VARIABLE_REF
				: CompletionProposal.FIELD_REF);

		TemplateModel value = modelValue;
		if (value instanceof TemplateMethodModel) {
			kind = CompletionProposal.METHOD_REF;
		}

		// Get key for item completion proposal
		String key = modelKey.toString();

		// Get description for item completion proposal
		String description = key;
		if (kind != CompletionProposal.METHOD_REF) {
			String s = AssistUtils.getValue(value);
			if (s != null) {
				description += " - " + s;	
			}
		}

		// Create CompletionProposal
		String path = paths[paths.length -1];
		addCompletionProposal(path.toCharArray(), key, description, kind);
	}

	/**
	 * Create and add {@link CompletionProposal}.
	 * 
	 * @param token
	 * @param key
	 * @param description
	 * @param kind
	 */
	private void addCompletionProposal(char[] token, String key,
			String description, int kind) {
		CompletionProposal proposal = this.createProposal(kind,
				this.actualCompletionPosition);
		// proposal.setSignature(getSignature(typeBinding));
		// proposal.setPackageName(q);
		// proposal.setTypeName(displayName);

		int relevance = computeBaseRelevance();
		relevance += computeRelevanceForInterestingProposal();
		relevance += computeRelevanceForCaseMatching(token, key);
		relevance += computeRelevanceForRestrictions(IAccessRule.K_ACCESSIBLE); // no

		proposal.setName(description);
		proposal.setCompletion(key);

		// proposal.setFlags(Flags.AccDefault);
		proposal.setReplaceRange(this.startPosition - this.offset,
				this.endPosition - this.offset);
		proposal.setRelevance(relevance);
		this.requestor.accept(proposal);
		if (DEBUG) {
			this.printDebug(proposal);
		}
	}

	/**
	 * Get the template file linked to the module.
	 * 
	 * @param module
	 * @return
	 */
	private IFile getTemplateFile(IModuleSource module) {
		IResource resource = module.getModelElement().getResource();
		if (!(resource instanceof IFile)) {
			return null;
		}
		return (IFile) resource;
	}

	/**
	 * Get the Freemarker {@link Configuration} from template file.
	 * 
	 * @param templateFile
	 * @return
	 */
	private Configuration getConfiguration(IFile templateFile) {
		if (templateFile == null) {
			return null;
		}
		try {
			return SettingsUtils.getConfiguration(templateFile);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * Get {@link TemplateEntriesModel} from the template file.
	 * 
	 * @param templateFile
	 * @return
	 */
	private TemplateEntriesModel getTemplateEntriesModel(IFile templateFile) {
		if (templateFile == null) {
			return null;
		}
		try {
			return SettingsUtils.getTemplateEntriesModel(templateFile);
		} catch (Throwable e) {
			return null;
		}
	}
}
