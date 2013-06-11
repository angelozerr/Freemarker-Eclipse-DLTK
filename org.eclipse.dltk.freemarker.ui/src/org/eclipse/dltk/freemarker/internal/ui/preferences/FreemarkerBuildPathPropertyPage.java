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

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.freemarker.core.FreemarkerLanguageToolkit;
import org.eclipse.dltk.ui.preferences.BuildPathsPropertyPage;
import org.eclipse.dltk.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Freemarker Build Path Project Property page.
 * 
 */
public class FreemarkerBuildPathPropertyPage extends BuildPathsPropertyPage
		implements IWorkbenchPropertyPage {
	
	public FreemarkerBuildPathPropertyPage() {
	}

	@Override
	protected BuildpathsBlock createBuildPathBlock(
			IWorkbenchPreferenceContainer pageContainer) {
		return new FreemarkerBuildPathsBlock(
				new BusyIndicatorRunnableContext(), this, getSettings().getInt(
						INDEX), false, pageContainer);
	}

	public IDLTKLanguageToolkit getLanguageToolkit() {
		return FreemarkerLanguageToolkit.getDefault();
	}
}
