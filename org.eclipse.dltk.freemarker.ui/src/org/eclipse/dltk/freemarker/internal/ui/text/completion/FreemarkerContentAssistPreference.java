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

import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;

/**
 * 
 * Freemarker DLTK {@link ContentAssistPreference} implementation.
 * 
 */
public class FreemarkerContentAssistPreference extends ContentAssistPreference {

	private static FreemarkerContentAssistPreference INSTANCE;

	protected ScriptTextTools getTextTools() {
		return FreemarkerUIPlugin.getDefault().getTextTools();
	}

	public static ContentAssistPreference getDefault() {
		if (INSTANCE == null) {
			INSTANCE = new FreemarkerContentAssistPreference();
		}
		return INSTANCE;
	}

}
