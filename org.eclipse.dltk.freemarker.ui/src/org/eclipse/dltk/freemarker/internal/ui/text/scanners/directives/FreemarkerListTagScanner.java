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

import java.util.List;

import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerWordDetector;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerColorConstants;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 *  Scanner for FTL 'list' <#list... >.
 *
 */
public class FreemarkerListTagScanner extends
		AbstractFreemarkerDirectiveScanner {

	public FreemarkerListTagScanner(TagSyntaxProvider tagSyntaxProvider,
			IColorManager manager, IPreferenceStore store) {
		super(LIST_DIRECTIVE, tagSyntaxProvider, manager, store);
		initialize();
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = super.createRules();
		IToken keyword = getToken(IFreemarkerColorConstants.FREEMARKER_KEYWORD);

		WordRule wordRule = new WordRule(new FreemarkerWordDetector());
		wordRule.addWord("as", keyword);
		rules.add(wordRule);

		return rules;
	}
}
