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
package org.eclipse.dltk.freemarker.internal.debug;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.debug.core.DLTKDebugPreferenceConstants;
import org.eclipse.dltk.freemarker.debug.FreemarkerDebugConstants;
import org.eclipse.dltk.freemarker.debug.FreemarkerDebugPlugin;

/**
 * Freemarker DLTK Debug preference initializer.
 * 
 */
public class FreemarkerDebugPreferenceInitializer extends
		AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		Preferences store = FreemarkerDebugPlugin.getDefault()
				.getPluginPreferences();

		store.setDefault(FreemarkerDebugConstants.DEBUGGING_ENGINE_ID_KEY, "");

		store.setDefault(
				DLTKDebugPreferenceConstants.PREF_DBGP_BREAK_ON_FIRST_LINE,
				false);
		store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_ENABLE_LOGGING,
				false);

		store.setDefault(
				DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_GLOBAL, true);
		store.setDefault(
				DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_CLASS, true);
		store.setDefault(
				DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_LOCAL, true);
	}

}
