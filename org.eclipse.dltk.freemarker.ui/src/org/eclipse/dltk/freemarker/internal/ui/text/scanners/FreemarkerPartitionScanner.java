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

import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerPartitions;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.comment.CommentsRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.AssignTagRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.IfTagRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.IncludeTagRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.directives.ListTagRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.interpolation.InterpolationRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * 
 * Freemarker {@link RuleBasedPartitionScanner} implementation.
 * 
 */
public class FreemarkerPartitionScanner extends RuleBasedPartitionScanner
		implements IFreemarkerPartitions {

	public FreemarkerPartitionScanner() {
		super();

		// TODO : get tagSyntax from Freemarker Configuration.
		TagSyntaxProvider tagSyntaxProvider = null;

		IToken comment = new Token(FREEMARKER_COMMENT);
		IToken assignDirective = new Token(FTL_ASSIGN);
		IToken includeDirective = new Token(FTL_INCLUDE);
		IToken ifDirective = new Token(FTL_IF_DIRECTIVE_START);
		IToken listDirective = new Token(FTL_LIST_DIRECTIVE_START);
		IToken interpolation = new Token(FTL_INTERPOLATION);

		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		// Add rules for FTL comments.
		rules.add(new CommentsRule(comment));

		// Add directives rules
		rules.add(new AssignTagRule(tagSyntaxProvider, assignDirective));
		rules.add(new IncludeTagRule(tagSyntaxProvider, includeDirective));
		rules.add(new IfTagRule(tagSyntaxProvider, ifDirective));
		rules.add(new ListTagRule(tagSyntaxProvider, listDirective));

		// Add Interpolation rules
		rules.add(new InterpolationRule(interpolation));
		
		IPredicateRule[] result = new IPredicateRule[rules.size()];

		rules.toArray(result);
		setPredicateRules(result);
	}
}

