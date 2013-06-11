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
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.text.folding.DefaultFoldingPreferenceConfigurationBlock;

/**
 * Freemarker editor folding options.
 */
public final class FreemarkerFoldingPreferencePage extends
		AbstractConfigurationBlockPreferencePage {

	protected String getHelpId() {
		// return IScriptHelpContextIds.JAVA_EDITOR_PREFERENCE_PAGE;
		return null;
	}

	protected void setDescription() {
		// setDescription(PreferencesMessages.EditorPreferencePage_folding_title);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(FreemarkerUIPlugin.getDefault().getPreferenceStore());
	}

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new DefaultFoldingPreferenceConfigurationBlock(
				overlayPreferenceStore, this) {
			// protected IFoldingPreferenceBlock createDocumentationBlock(
			// OverlayPreferenceStore store, PreferencePage page) {
			// return new FreemarkerDocFoldingPreferenceBlock(store, page);
			// }
			//
			// protected IFoldingPreferenceBlock createSourceCodeBlock(
			// OverlayPreferenceStore store, PreferencePage page) {
			// return new FreemarkerFoldingPreferenceBlock(store, page);
			// }
		};
	}
}
