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
package org.eclipse.dltk.freemarker.core.util;

import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.freemarker.core.FreemarkerCorePlugin;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerSettingsManager;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerTemplateManager;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.provider.IConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;

import freemarker.provisionnal.ext.ide.TemplateEntriesModel;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModelException;

/**
 * Settings Utilities.
 * 
 */
public class SettingsUtils {

	private static final Configuration DEFAULT_CONFIGURATION = new Configuration();

	/**
	 * Get the data model for the templateFile.
	 * 
	 * @param templateFile
	 * @return
	 * @throws InstanceProviderException
	 */
	public static Object getModel(IFile templateFile)
			throws InstanceProviderException {
		Object model = getModelFromSettings(templateFile);
		if (model != null) {
			return model;
		}
		return Collections.EMPTY_MAP;
	}

	/**
	 * Get the data-model from the template settings.
	 * 
	 * @param templateFile
	 * @return
	 * @throws InstanceProviderException
	 */
	public static Object getModelFromSettings(IFile templateFile)
			throws InstanceProviderException {
		IFreemarkerTemplateSettings settings = getTemplateSettings(templateFile);
		if (settings != null) {
			IModelInstanceProvider modelProvider = settings.getModelProvider();
			if (modelProvider != null) {
				return modelProvider.getModel();
			}
		}
		return null;
	}

	/**
	 * Return Freemarker Configuration initialized with base dir of the template
	 * file.
	 * 
	 * @return
	 */
	public static Configuration getConfiguration(IFile templateFile)
			throws InstanceProviderException {
		Configuration configuration = getConfigurationFromSettings(templateFile);
		if (configuration == null) {
			configuration = new Configuration();
			// Initialize FM Configuration.
			// Set the directory base dir for Freemarker template loading with
			// the directory which contains this file to manage <#import '...'>.
			if (templateFile != null && templateFile.exists()) {
				try {
					FreemarkerTemplateManager.getManager()
							.prepareConfiguration(configuration, templateFile);
				} catch (Throwable e) {
					FreemarkerCorePlugin.log(e);
				}
			}
		}
		return configuration;
	}

	/**
	 * Get the configuration from the template settings.
	 * 
	 * @param templateFile
	 * @return
	 * @throws InstanceProviderException
	 */
	private static Configuration getConfigurationFromSettings(IFile templateFile)
			throws InstanceProviderException {

		IFreemarkerTemplateSettings settings = getTemplateSettings(templateFile);
		if (settings != null) {
			IConfigurationInstanceProvider configurationProvider = settings
					.getConfigurationProvider();
			if (configurationProvider != null) {
				return (Configuration) configurationProvider.getConfiguration();
			}
		}
		return null;
	}

	/**
	 * return the template settings linked to the templateFile.
	 * 
	 * @param templateFile
	 * @return
	 */
	private static IFreemarkerTemplateSettings getTemplateSettings(
			IFile templateFile) {
		try {
			return FreemarkerSettingsManager.getManager().getTemplateSettings(
					templateFile);
		} catch (CoreException e) {
			FreemarkerCorePlugin.log(e);
		}
		return null;
	}


	/**
	 * Get the {@link TemplateHashModelEx} by using the model coming from the
	 * template settings.
	 * 
	 * @param modelProvider
	 * @param configurationProvider
	 * @return
	 * @throws InstanceProviderException
	 * @throws TemplateModelException
	 */
	public static TemplateEntriesModel getTemplateEntriesModel(
			IFile templateFile) throws InstanceProviderException,
			TemplateModelException {
		IFreemarkerTemplateSettings templateSettings = getTemplateSettings(templateFile);
		if (templateSettings == null) {
			return null;
		}
		IModelInstanceProvider modelProvider = templateSettings
				.getModelProvider();
		IConfigurationInstanceProvider configurationProvider = templateSettings
				.getConfigurationProvider();
		return getTemplateEntriesModel(modelProvider, configurationProvider);
	}
	
	/**
	 * Get the {@link TemplateHashModelEx} by using the model coming from the
	 * modelProvider and the configuration coming from the
	 * configurationProvider.
	 * 
	 * @param modelProvider
	 * @param configurationProvider
	 * @return
	 * @throws InstanceProviderException
	 * @throws TemplateModelException
	 */
	public static TemplateEntriesModel getTemplateEntriesModel(
			IModelInstanceProvider modelProvider,
			IConfigurationInstanceProvider configurationProvider)
			throws InstanceProviderException, TemplateModelException {

		// Get the model
		if (modelProvider == null) {
			return null;
		}
		Object rootMap = modelProvider.getModel();
		if (rootMap == null) {
			return null;
		}

		// Get the Freemarker Configuration
		Configuration configuration = DEFAULT_CONFIGURATION;
		if (configurationProvider != null) {
			configuration = configurationProvider.getConfiguration();
		}

		return modelProvider.getTemplateEntriesModel(configuration);
		

	}
}
