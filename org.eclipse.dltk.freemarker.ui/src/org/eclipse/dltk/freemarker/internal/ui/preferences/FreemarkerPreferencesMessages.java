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
package org.eclipse.dltk.freemarker.internal.ui.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * Messages used into Freemarker UI Prefrences pages bounded with
 * FreemarkerPreferencesMessages.properties. file.
 * 
 */
public final class FreemarkerPreferencesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.dltk.freemarker.internal.ui.preferences.FreemarkerPreferencesMessages"; //$NON-NLS-1$

	static {
		NLS
				.initializeMessages(BUNDLE_NAME,
						FreemarkerPreferencesMessages.class);
	}

	private FreemarkerPreferencesMessages() {
	}

	public static String EditorPreferencePageDescription;

	public static String GlobalPreferencePageDescription;
	
	public static String FreemarkerEditorPreferencePage_expression;
	public static String FreemarkerEditorPreferencePage_interpolation;

}
