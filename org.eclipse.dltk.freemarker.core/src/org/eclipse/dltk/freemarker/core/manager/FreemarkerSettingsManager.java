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
package org.eclipse.dltk.freemarker.core.manager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.internal.core.settings.FreemarkerProjectSettings;

/**
 * Freemarker settings Manager.
 * 
 */
public class FreemarkerSettingsManager extends
		ClassLoaderProjectManager<IFreemarkerProjectSettings> {

	private static final FreemarkerSettingsManager INSTANCE = new FreemarkerSettingsManager();

	public static FreemarkerSettingsManager getManager() {
		return INSTANCE;
	}

	/**
	 * Create Freemarker project settings.
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public IFreemarkerProjectSettings getProjectSettings(IProject project)
			throws CoreException {
		IFreemarkerProjectSettings settings = FreemarkerProjectSettings.findProjectSettings(project);
		super.addProject(settings);
		return settings;
	}

	/**
	 * Create or get Freemarker Template settings.
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public IFreemarkerTemplateSettings getTemplateSettings(IFile file) throws CoreException {
		return FreemarkerProjectSettings.findTemplateSettings(file);
	}

}
