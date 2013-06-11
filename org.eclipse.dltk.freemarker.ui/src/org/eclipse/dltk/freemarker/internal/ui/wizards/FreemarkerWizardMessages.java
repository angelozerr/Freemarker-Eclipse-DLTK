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

import org.eclipse.osgi.util.NLS;

public class FreemarkerWizardMessages {

	private static final String BUNDLE_NAME= "org.eclipse.dltk.freemarker.internal.ui.wizards.FreemarkerWizardMessages";//$NON-NLS-1$

	public static String FileCreationWizard_title;

	public static String ProjectCreationWizard_title;
	public static String ProjectCreationWizardFirstPage_title;
	public static String ProjectCreationWizardFirstPage_description;
		
	static {
		NLS.initializeMessages(BUNDLE_NAME, FreemarkerWizardMessages.class);
	}
}
