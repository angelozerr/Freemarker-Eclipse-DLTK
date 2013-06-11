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
package org.eclipse.dltk.freemarker.internal.debug.ui.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * Messages used into Freemarker Debug Prefrences pages bounded with
 * FreemarkerDebugPreferencesMessages.properties. file.
 * 
 */
public class FreemarkerDebugPreferencesMessages {

	private static final String BUNDLE_NAME = "org.eclipse.dltk.freemarker.internal.debug.ui.preferences.FreemarkerDebugPreferencesMessages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME,
				FreemarkerDebugPreferencesMessages.class);
	}

	public static String FreemarkerDebugPreferencePage_description;
	public static String FreemarkerDebugEnginePreferencePage_description;
}
