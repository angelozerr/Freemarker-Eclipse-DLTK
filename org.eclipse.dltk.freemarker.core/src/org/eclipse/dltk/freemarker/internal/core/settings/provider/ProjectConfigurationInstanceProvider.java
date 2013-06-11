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
package org.eclipse.dltk.freemarker.internal.core.settings.provider;

import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;

import freemarker.template.Configuration;

/**
 * 
 * {@link Configuration} instance provider implementation with
 * {@link SettingsScope#PROJECT} scope.
 * 
 */
public class ProjectConfigurationInstanceProvider extends
		ConfigurationInstanceProvider implements IProjectObjectInstanceProvider {

	private String id = null;

	private String name;

	private boolean defaultConfig;

	public ProjectConfigurationInstanceProvider(IResource resource,
			IFreemarkerProjectSettings project) {
		super(resource, project, SettingsScope.PROJECT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.core.settings.provider.
	 * IProjectObjectInstanceProvider#getId()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.core.settings.provider.
	 * IProjectObjectInstanceProvider#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.core.settings.provider.
	 * IProjectObjectInstanceProvider#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.core.settings.provider.
	 * IProjectObjectInstanceProvider#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.core.settings.provider.
	 * IProjectObjectInstanceProvider#isDefaultConfig()
	 */
	public boolean isDefaultConfig() {
		return defaultConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.core.settings.provider.
	 * IProjectObjectInstanceProvider#setDefaultConfig(boolean)
	 */
	public void setDefaultConfig(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	@Override
	public void reset() {		
		super.reset();
		this.name = null;
		this.defaultConfig = false;
	}
}
