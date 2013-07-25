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
package org.eclipse.dltk.freemarker.internal.core.parser;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.ast.parser.IModuleDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerTemplateManager;
import org.eclipse.dltk.freemarker.core.util.SettingsUtils;
import org.eclipse.dltk.freemarker.internal.core.parser.visitors.DLTKFreemarkerASTVisitor;
import org.eclipse.dltk.freemarker.internal.core.util.TokenUtils;

import freemarker.core.ParseException;
import freemarker.core.TemplateElement;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker DLTK {@link ISourceParser} implementation.
 * 
 */
public class FreemarkerSourceParser extends AbstractSourceParser {

	public IModuleDeclaration parse(IModuleSource input,
			IProblemReporter reporter) {
		String fileName = input.getFileName();
		char[] source = input.getContentsAsCharArray();

		IFile templateFile = getTemplateFile(input);
		
		// Get configuration from template settings (or default configuration if is null).
		Configuration configuration = getConfiguration(templateFile);
		
		// Get model from template settings (can be null).
		Object model = getModelFromSettings(templateFile);

		// Parse FM content
		// @Jonathan : is it possible to parse FM and get it the Root even if
		// there are errors?
		// If your template contains 2 errors, it display just one error into
		// the editor.
		TemplateElement root = parseFM(fileName, source, configuration, model,
				reporter);

		DLTKFreemarkerASTVisitor visitor = getASTBuilderVisitor(source);
		/*if (root != null)
			visitor.visit(root);
		visitor.getModuleDeclaration().rebuild();
		return visitor.getModuleDeclaration();*/
		return null;
	}

	private DLTKFreemarkerASTVisitor getASTBuilderVisitor(char[] source) {
		return null;// new DLTKFreemarkerASTVisitor(source);
	}

	private TemplateElement parseFM(String fileName, char[] source,
			Configuration configuration, Object model, IProblemReporter reporter) {

		String templateName = getTemplateName(fileName);

		Reader reader = new CharArrayReader(source);
		Template template = null;
		TemplateElement root = null;
		try {
			/* Create Freemarker template */
			template = FreemarkerTemplateManager.getManager().process(templateName,
					reader, configuration, model, null, null);
		} catch (ParseException e) {
			// Syntax error

			// FIXME @Jonathan : Token doesn't manage start/end offset and
			// content of char
			// array (source) must be loop to compute it.
			// Use startOffset/endOffeset Token when it will be implemented.

			int lineNumber = e.getLineNumber();
			int columnNumber = e.getColumnNumber();

			int beginColumn = 0;
			int beginLine = 0;
			int endColumn = 0;
			int endLine = 0;
			/*Token token = e.currentToken;
			// if (token != null) {
			// token = token.next;
			// }

			if (token != null) {
				beginLine = token.getBeginLine();
				endLine = token.getEndLine();
				beginColumn = token.getBeginColumn();
				endColumn = token.getEndColumn();
			} else {
			*/
				beginLine = lineNumber;
				endLine = lineNumber;
				beginColumn = columnNumber;
				endColumn = columnNumber;
			//}

			int[] offsets = TokenUtils.getOffsets(source, beginLine, endLine,
					beginColumn, endColumn);
			int startOffset = offsets[0];
			if (startOffset == -1) {
				startOffset = lineNumber;
			}
			int endOffset = offsets[1];
			if (endOffset == -1) {
				endOffset = columnNumber;
			}

			// Create DLTK IProblem by using Token error

			IProblem problem = new DefaultProblem(templateName, e.getMessage(),
					IProblem.Syntax, new String[0], ProblemSeverities.Error,
					startOffset, endOffset, lineNumber);

			reporter.reportProblem(problem);
		} catch (TemplateException e) {
			// Model error

			int startOffset = 1;
			int endOffset = 2;
			int lineNumber = 1;

			/* FIXME : offset
			 * List<TemplateLocation> locations = e.getFTLStack();
			if (locations != null && !locations.isEmpty()) {
				TemplateLocation token = locations.get(locations.size() - 2);

				int beginLine = token.getBeginLine();
				int endLine = token.getEndLine();
				int beginColumn = token.getBeginColumn();
				int endColumn = token.getEndColumn();

				int[] offsets = TokenUtils.getOffsets(source, beginLine,
						endLine, beginColumn, endColumn);
				startOffset = offsets[0];
				if (startOffset == -1) {
					startOffset = lineNumber;
				}
				endOffset = offsets[1];
				// if (endOffset == -1) {
				// endOffset = columnNumber;
				// }

			}*/

			IProblem problem = new DefaultProblem(templateName, e.getMessage(),
					IProblem.FieldRelated, new String[0],
					ProblemSeverities.Error, startOffset, endOffset, lineNumber);

			reporter.reportProblem(problem);
		} catch (Throwable e) {
			// FIXME @Jonathan : problem with FMParser which throw
			// NullPointerException
			// with the content
			/* <#if true ><#else></#end> */

			String message = e.getMessage();
			if (e instanceof NullPointerException) {
				message = "NullPointerException";
			}

			IProblem problem = new DefaultProblem(templateName, message,
					IProblem.Unclassified, new String[0],
					ProblemSeverities.Error, 1, 2, 1);

			reporter.reportProblem(problem);
		}
		if (template != null) {
			// Template was built, get the FM Root.
			root = template.getRootTreeNode();
		}
		return root;
	}

	/**
	 * Get the template name from the fileName. The first path (which is teh
	 * Eclipse Project Path) must be removed in order to than <#include
	 * directive works..
	 * 
	 * <p>
	 * ex : fileName='/Test-FTL-Java/test.ftl' will return 'test.ftl'.
	 * </p>
	 * 
	 * @param fileName
	 * @return
	 */
	private String getTemplateName(String fileName) {
		String templateName = fileName;
		int index = templateName.lastIndexOf("/");
		// if (templateName.startsWith("/")) {
		// index = templateName.indexOf("/", 1);
		// } else {
		// index = templateName.indexOf("/");
		// }
		if (index != -1) {
			// remove project path (otherwise there is problem with include
			// directive).
			templateName = templateName.substring(index + 1,
					templateName.length());
		}
		return templateName;
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
			return new Configuration();
		}
	}

	/**
	 * Get the Data-Model from template file.
	 * 
	 * @param templateFile
	 * @return
	 */
	private Object getModelFromSettings(IFile templateFile) {
		if (templateFile == null) {
			return null;
		}
		try {
			return SettingsUtils.getModelFromSettings(templateFile);
		} catch (Throwable e) {
			return Collections.emptyMap();
		}
	}
}
