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
package org.eclipse.dltk.freemarker.internal.ui.wizards;

import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.NewSourceModulePage;
import org.eclipse.dltk.ui.wizards.NewSourceModuleWizard;

/**
 * Wizard to create Freemarker file.
 *
 */
public class FreemarkerNewFileWizard extends NewSourceModuleWizard {

	public static final String WIZARD_ID = "org.eclipse.dltk.freemarker.wizards.newfile"; //$NON-NLS-1$

	public FreemarkerNewFileWizard() {
		setDefaultPageImageDescriptor(FreemarkerUIPluginImages.DESC_WIZBAN_PROJECT_CREATION);
		setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
		setWindowTitle(FreemarkerWizardMessages.FileCreationWizard_title);
	}

	protected NewSourceModulePage createNewSourceModulePage() {
		return new NewSourceModulePage() {

			protected String getRequiredNature() {
				return FreemarkerNature.NATURE_ID;
			}

			protected String getPageDescription() {
				return "This wizard creates a new Freemarker file.";
			}

			protected String getPageTitle() {
				return "Create new Freemarker file";
			}
		};
	}
}
