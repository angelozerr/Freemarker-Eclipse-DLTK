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
package org.eclipse.dltk.freemarker.internal.ui.text.rules.comment;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

/**
 *  JFace {@link IRule} for FTL Comments <#-- xxx -->.
 *
 */
public class CommentsRule extends MultiLineRule {

	public static final String START_COMMENTS = "<#--";
	public static final String END_COMMENTS = "-->";
	

	public CommentsRule(IToken token) {
		super(START_COMMENTS, END_COMMENTS, token, '\0', true);
	}

}
