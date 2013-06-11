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
package org.eclipse.dltk.freemarker.internal.ui.text.completion;

import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProcessor;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.IEditorPart;

/**
 * Freemarke completion processor
 */
public class FreemarkerCompletionProcessor extends ScriptCompletionProcessor {

	public FreemarkerCompletionProcessor(IEditorPart editor,
			ContentAssistant assistant, String partition) {
		super(editor, assistant, partition);
	}

	/*
	 * @see ScriptCompletionProcessor#getNatureId()
	 */
	protected String getNatureId() {
		return FreemarkerNature.NATURE_ID;
	}

}
