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
package org.eclipse.dltk.freemarker.internal.ui.text.scanners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerColorConstants;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.AssignTagRuleEnd;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.ElseTagRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.IfTagRuleEnd;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.ListTagRuleEnd;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * Freemarker code scanner.
 * 
 */
public class FreemarkerCodeScanner extends AbstractFreemarkerScanner {

	private static final String[] fgTokenProperties = {
			IFreemarkerColorConstants.FREEMARKER_DEFAULT,
			IFreemarkerColorConstants.FREEMARKER_KEYWORD };

	public FreemarkerCodeScanner(TagSyntaxProvider tagSyntaxProvider,
			IColorManager manager, IPreferenceStore store) {
		super(tagSyntaxProvider, manager, store);
		initialize();
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		IToken keyword = getToken(IFreemarkerColorConstants.FREEMARKER_KEYWORD);
		IToken other = getToken(IFreemarkerColorConstants.FREEMARKER_DEFAULT);

		// Directive End Rules.
		rules.add(new AssignTagRuleEnd(tagSyntaxProvider, keyword));
		rules.add(new IfTagRuleEnd(tagSyntaxProvider, keyword));
		rules.add(new ElseTagRule(tagSyntaxProvider, keyword));
		rules.add(new ListTagRuleEnd(tagSyntaxProvider, keyword));

		setDefaultReturnToken(other);
		return rules;
	}

	@Override
	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

}
