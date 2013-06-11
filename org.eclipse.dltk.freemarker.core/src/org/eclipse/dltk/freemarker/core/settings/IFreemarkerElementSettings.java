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
package org.eclipse.dltk.freemarker.core.settings;

import org.eclipse.core.runtime.CoreException;

/**
 * Base freemarker element settings.
 * 
 */
public interface IFreemarkerElementSettings extends
		IFreemarkerSettingsConstants {

	/**
	 * Load the settings.
	 * 
	 * @throws CoreException
	 */
	void load() throws CoreException;
	
	/**
	 * Load the settings.
	 * 
	 * @throws CoreException
	 */
	void load(boolean force) throws CoreException;

	/**
	 * Save the settings.
	 * 
	 * @throws CoreException
	 */
	void save() throws CoreException;

	/**
	 * Returns the owner freemarker project settings.
	 * 
	 * @return
	 * @throws CoreException
	 */
	IFreemarkerProjectSettings getProjectSettings() throws CoreException;
	
	void addFreemarkerSettingsChangedListener(IFreemarkerSettingsChangedListener listener);
	
	void removeFreemarkerSettingsChangedListener(IFreemarkerSettingsChangedListener listener);
	
	SettingsScope getScope();
}
