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
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlock;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Freemarker DLTK Root Preferences Page that you can access at
 * Window/Preferences->Freemarker.
 * 
 */
public class FreemarkerGlobalPreferencesPage extends
		AbstractConfigurationBlockPreferencePage {

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new AbstractConfigurationBlock(overlayPreferenceStore, this) {
			public Control createControl(Composite parent) {
				Composite composite = SWTFactory.createComposite(parent, parent
						.getFont(), 1, 1, GridData.FILL_HORIZONTAL);
				return composite;
			}
		};
	}

	protected String getHelpId() {
		return null;
	}

	protected void setDescription() {
		setDescription(FreemarkerPreferencesMessages.GlobalPreferencePageDescription);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(FreemarkerUIPlugin.getDefault().getPreferenceStore());
	}

}
