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
package org.eclipse.dltk.freemarker.internal.ui.templates;

import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.dltk.ui.templates.ScriptTemplateCompletionProcessor;
import org.eclipse.dltk.ui.text.completion.ScriptContentAssistInvocationContext;

/**
 * Freemarker template completion processor.
 * 
 */
public class FreemarkerTemplateCompletionProcessor extends
		ScriptTemplateCompletionProcessor {

	// private static char[] IGNORE = new char[] {'.', ':', '@', '$'};

	public FreemarkerTemplateCompletionProcessor(
			ScriptContentAssistInvocationContext context) {
		super(context);
	}

	/*
	 * @seeorg.eclipse.dltk.ui.templates.ScriptTemplateCompletionProcessor#
	 * getContextTypeId()
	 */
	protected String getContextTypeId() {
		return FreemarkerUniversalTemplateContextType.CONTEXT_TYPE_ID;
	}

	/*
	 * @see
	 * org.eclipse.dltk.ui.templates.ScriptTemplateCompletionProcessor#getIgnore
	 * ()
	 */
	// protected char[] getIgnore() {
	// return IGNORE;
	// }

	/*
	 * @seeorg.eclipse.dltk.ui.templates.ScriptTemplateCompletionProcessor#
	 * getTemplateAccess()
	 */
	protected ScriptTemplateAccess getTemplateAccess() {
		return FreemarkerTemplateAccess.getInstance();
	}

}
