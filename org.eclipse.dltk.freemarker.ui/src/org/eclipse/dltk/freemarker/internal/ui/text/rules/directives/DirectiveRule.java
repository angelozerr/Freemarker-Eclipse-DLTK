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

import org.eclipse.dltk.freemarker.internal.ui.text.rules.FTLTagRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * JFace {@link IRule} base class for FTL Directive Start.
 *
 */
public class DirectiveRule extends FTLTagRule implements IDirectiveConstants {

	private static final char TAG_INDENTIFIER_CHAR = '#';

	public DirectiveRule(String tagName, TagSyntaxProvider tagSyntaxProvider,
			IToken token) {
		super(TAG_INDENTIFIER_CHAR + tagName, tagSyntaxProvider, token, '\0',
				true);
	}

}