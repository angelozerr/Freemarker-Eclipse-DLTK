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
package org.eclipse.dltk.freemarker.internal.ui.text.rules.directives;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * JFace {@link IRule} for FTL 'assign' End : </#assign>.
 * 
 */
public class AssignTagRuleEnd extends DirectiveRuleEnd {

	public AssignTagRuleEnd(TagSyntaxProvider tagSyntaxProvider, IToken token) {
		super(ASSIGN_DIRECTIVE, tagSyntaxProvider, token);
	}

}
