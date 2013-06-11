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

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.freemarker.core.FreemarkerCorePlugin;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerTemplateManager;
import org.eclipse.dltk.freemarker.core.settings.IClassLoaderProject;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;

import freemarker.template.Configuration;

/**
 * 
 * {@link Configuration} instance provider implementation with
 * {@link SettingsScope#TEMPLATE} scope.
 * 
 */
public class ConfigurationInstanceProvider extends BaseObjectInstanceProvider
		implements IConfigurationInstanceProvider {

	private Configuration currentConfiguration = null;

	public ConfigurationInstanceProvider(IResource resource,
			IFreemarkerProjectSettings project) {
		this(resource, project, SettingsScope.TEMPLATE);
	}

	public ConfigurationInstanceProvider(IResource resource,
			IFreemarkerProjectSettings project, SettingsScope scope) {
		super(resource, project, ProviderType.CONFIGURATION, scope);
	}

	public Configuration getConfiguration() throws InstanceProviderException {

		Configuration configuration = (Configuration) super.getInstance();
		if (configuration == null) {
			configuration = new Configuration();

		}
		if (currentConfiguration == null
				|| !currentConfiguration.equals(configuration)) {
			try {
				FreemarkerTemplateManager.getManager().prepareConfiguration(
						configuration, resource);
			} catch (IOException e) {
				FreemarkerCorePlugin.log(e);
			}
			currentConfiguration = configuration;
		}
		return currentConfiguration;
	}

}
