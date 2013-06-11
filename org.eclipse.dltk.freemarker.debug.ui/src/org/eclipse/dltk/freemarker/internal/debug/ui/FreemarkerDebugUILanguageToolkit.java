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
package org.eclipse.dltk.freemarker.internal.debug.ui;

import org.eclipse.dltk.debug.ui.AbstractDebugUILanguageToolkit;
import org.eclipse.dltk.debug.ui.IDLTKDebugUILanguageToolkit;
import org.eclipse.dltk.freemarker.debug.FreemarkerDebugConstants;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Freemarker Debug {@link IDLTKDebugUILanguageToolkit} implementation.
 * 
 */
public class FreemarkerDebugUILanguageToolkit extends
		AbstractDebugUILanguageToolkit {

	/*
	 * @see
	 * org.eclipse.dltk.debug.ui.IDLTKDebugUILanguageToolkit#getDebugModelId()
	 */
	public String getDebugModelId() {
		return FreemarkerDebugConstants.DEBUG_MODEL_ID;
	}

	/*
	 * @see
	 * org.eclipse.dltk.debug.ui.IDLTKDebugUILanguageToolkit#getPreferenceStore
	 * ()
	 */
	public IPreferenceStore getPreferenceStore() {
		return FreemarkerDebugUIPlugin.getDefault().getPreferenceStore();
	}

	/*
	 * @seeorg.eclipse.dltk.debug.ui.AbstractDebugUILanguageToolkit#
	 * getVariablesViewPreferencePages()
	 */
	public String[] getVariablesViewPreferencePages() {
		return new String[] { "org.eclipse.dltk.freemarker.preferences.debug.detailFormatters" };
	}
}
