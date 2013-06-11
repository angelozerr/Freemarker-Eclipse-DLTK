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

import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * 
 * Freemarker template access to load completion templates from templates/templates.xml file.
 *
 */
public class FreemarkerTemplateAccess extends ScriptTemplateAccess {
	// Template
	private static final String CUSTOM_TEMPLATES_KEY = "org.eclipse.freemarker.Templates"; //$NON-NLS-1$

	private static FreemarkerTemplateAccess instance;

	public static FreemarkerTemplateAccess getInstance() {
		if (instance == null) {
			instance = new FreemarkerTemplateAccess();
		}

		return instance;
	}

	/*
	 * @see
	 * org.eclipse.dltk.ui.templates.ScriptTemplateAccess#getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return FreemarkerUIPlugin.getDefault().getPreferenceStore();
	}

	/*
	 * @see
	 * org.eclipse.dltk.ui.templates.ScriptTemplateAccess#getContextTypeId()
	 */
	protected String getContextTypeId() {
		return FreemarkerUniversalTemplateContextType.CONTEXT_TYPE_ID;
	}

	/*
	 * @see
	 * org.eclipse.dltk.ui.templates.ScriptTemplateAccess#getCustomTemplatesKey
	 * ()
	 */
	protected String getCustomTemplatesKey() {
		return CUSTOM_TEMPLATES_KEY;
	}

}
