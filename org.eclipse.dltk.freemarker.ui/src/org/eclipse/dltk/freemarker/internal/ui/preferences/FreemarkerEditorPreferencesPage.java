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

import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.EditorConfigurationBlock;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;

/**
 * Freemarker DLTK Editor Preferences Page that you can access at
 * Window/Preferences->Freemarker/Editor.
 * 
 */
public class FreemarkerEditorPreferencesPage extends
		AbstractConfigurationBlockPreferencePage {

	protected String getHelpId() {
		return null;
	}

	protected void setDescription() {
		setDescription(FreemarkerPreferencesMessages.EditorPreferencePageDescription);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(FreemarkerUIPlugin.getDefault().getPreferenceStore());
	}

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		// Create DLTK standard editor block
		return new EditorConfigurationBlock(this, overlayPreferenceStore);
	}

}
