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

import org.eclipse.dltk.core.IPreferencesLookupDelegate;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.templates.IScriptTemplateIndenter;
import org.eclipse.dltk.ui.templates.ScriptTemplateContext;
import org.eclipse.dltk.ui.templates.TabExpandScriptTemplateIndenter;
import org.eclipse.dltk.ui.text.util.TabStyle;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.TemplateContextType;

/**
 * 
 * Freemarker template context.
 *
 */
public class FreemarkerTemplateContext extends ScriptTemplateContext {

	public FreemarkerTemplateContext(TemplateContextType type,
			IDocument document, int completionOffset, int completionLength,
			ISourceModule sourceModule) {
		super(type, document, completionOffset, completionLength, sourceModule);
	}

	/*
	 * @see org.eclipse.dltk.ui.templates.ScriptTemplateContext#getIndenter()
	 */
	protected IScriptTemplateIndenter getIndenter() {
		IPreferencesLookupDelegate prefs = getPreferences();
		if (TabStyle.SPACES == TabStyle.forName(prefs.getString(
				FreemarkerUIPlugin.PLUGIN_ID,
				CodeFormatterConstants.FORMATTER_TAB_CHAR))) {
			return new TabExpandScriptTemplateIndenter(prefs.getInt(
					FreemarkerUIPlugin.PLUGIN_ID,
					CodeFormatterConstants.FORMATTER_TAB_SIZE));
		}
		return super.getIndenter();
	}

}
