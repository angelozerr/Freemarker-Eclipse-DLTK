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
package org.eclipse.dltk.freemarker.internal.ui.text.scanners.directives;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerWhitespaceDetector;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerColorConstants;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.FTLEndTagRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.FTLStartTagRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.IDirectiveConstants;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.AbstractFreemarkerScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WhitespaceRule;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * Base class for Directive scanner.
 * 
 */
public class AbstractFreemarkerDirectiveScanner extends
		AbstractFreemarkerScanner implements IDirectiveConstants {

	private String tagName;

	private static final String[] fgTokenProperties = new String[] {
			IFreemarkerColorConstants.FREEMARKER_EXPRESSION,
			IFreemarkerColorConstants.FREEMARKER_KEYWORD };

	public AbstractFreemarkerDirectiveScanner(String tagName,
			TagSyntaxProvider tagSyntaxProvider, IColorManager manager,
			IPreferenceStore store) {
		super(tagSyntaxProvider, manager, store);
		this.tagName = tagName;
		initialize();
	}

	@Override
	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();

		IToken expression = getToken(IFreemarkerColorConstants.FREEMARKER_EXPRESSION);
		IToken keyword = getToken(IFreemarkerColorConstants.FREEMARKER_KEYWORD);

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new FreemarkerWhitespaceDetector()));

		rules.add(new FTLStartTagRule("<#" + tagName, tagSyntaxProvider,
				keyword));
		rules.add(new FTLEndTagRule(tagSyntaxProvider, keyword));

		setDefaultReturnToken(expression);
		return rules;
	}

}
