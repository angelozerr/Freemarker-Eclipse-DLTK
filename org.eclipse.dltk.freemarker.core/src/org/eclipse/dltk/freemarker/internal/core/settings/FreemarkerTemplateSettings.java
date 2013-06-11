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
package org.eclipse.dltk.freemarker.internal.core.settings;

import static org.eclipse.dltk.freemarker.core.util.XMLUtils.addAttribute;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.internal.core.settings.provider.ConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.internal.core.settings.provider.ModelInstanceProvider;
import org.xml.sax.Attributes;

/**
 * Freemarker template settings implementatuon.
 * 
 */
public class FreemarkerTemplateSettings extends
		FreemarkerElementSettings<IFile> implements IFreemarkerTemplateSettings {

	private static final String TEMPLATE_ELT = "template";

	// Model provider
	private IModelInstanceProvider modelProviderProjectScope;
	private IModelInstanceProvider modelProviderTemplateScope;
	private SettingsScope modelProviderScope = SettingsScope.NONE;

	// Configuration provider
	private IConfigurationInstanceProvider configurationProviderProjectScope;
	private IConfigurationInstanceProvider configurationProviderTemplateScope;
	private SettingsScope configurationProviderScope = SettingsScope.NONE;

	public FreemarkerTemplateSettings(IFile resource) {
		super(resource);
	}

	@Override
	protected String getRootElementName() {
		return TEMPLATE_ELT;
	}

	// ------------------ Model Provider

	/**
	 * Returns the model provider scope (Project or Template).
	 */
	public SettingsScope getModelProviderScope() {
		return modelProviderScope;
	}

	/**
	 * Set the model provider scope (Project or Template).
	 */
	public void setModelProviderScope(SettingsScope modelProviderScope) {
		if (this.modelProviderScope != modelProviderScope) {
			// scope is different, reset the model provider project and template
			// scope.
			modelProviderProjectScope = null;
			if (modelProviderTemplateScope != null) {
				modelProviderTemplateScope.reset();
			}
		}
		this.modelProviderScope = modelProviderScope;
	}

	/**
	 * Returns the active model provider switch the model provider scope.
	 */
	public IModelInstanceProvider getModelProvider() {
		if (modelProviderScope == SettingsScope.NONE) {
			return null;
		}
		switch (modelProviderScope) {
		case PROJECT:
			return modelProviderProjectScope;
		case TEMPLATE:
			return getModelProviderTemplateScope();
		}
		return null;
	}

	/**
	 * Returns the model provider template scope.
	 * 
	 * @return
	 */
	private IModelInstanceProvider getModelProviderTemplateScope() {
		if (modelProviderTemplateScope == null) {
			modelProviderTemplateScope = createModelProviderTemplateScope();
		}
		return modelProviderTemplateScope;
	}

	/**
	 * Create model provider template scope.
	 * 
	 * @return
	 */
	private IModelInstanceProvider createModelProviderTemplateScope() {
		try {
			return new ModelInstanceProvider(resource, getProjectSettings());
		} catch (CoreException e) {
		}
		return null;
	}

	/**
	 * Set the model provider and initialize the scope.
	 */
	public void setModelProvider(IModelInstanceProvider modelProvider) {
		if (modelProvider == null) {
			setModelProviderScope(SettingsScope.NONE);			
			return;	
		}
		switch (modelProvider.getScope()) {
		case PROJECT:
			setModelProviderScope(SettingsScope.PROJECT);
			this.modelProviderProjectScope = modelProvider;
			break;
		case TEMPLATE:
			setModelProviderScope(SettingsScope.TEMPLATE);
			this.modelProviderTemplateScope = modelProvider;
			break;
		}
	}

	// ------------------ Configuration Provider

	/**
	 * Returns the configuration provider scope (Project or Template).
	 */
	public SettingsScope getConfigurationProviderScope() {
		return configurationProviderScope;
	}

	/**
	 * Set the configuration provider scope (Project or Template).
	 */
	public void setConfigurationProviderScope(
			SettingsScope configurationProviderScope) {
		if (this.configurationProviderScope != configurationProviderScope) {
			// scope is different, reset the configuration provider project and
			// template
			// scope.
			configurationProviderProjectScope = null;
			if (configurationProviderTemplateScope != null) {
				configurationProviderTemplateScope.reset();
			}
		}
		this.configurationProviderScope = configurationProviderScope;
	}

	/**
	 * Returns the active configuration provider switch the configuration
	 * provider scope.
	 */
	public IConfigurationInstanceProvider getConfigurationProvider() {
		if (configurationProviderScope == SettingsScope.NONE) {
			return null;
		}
		switch (configurationProviderScope) {
		case PROJECT:
			return configurationProviderProjectScope;
		case TEMPLATE:
			return getConfigurationProviderTemplateScope();
		}
		return null;
	}

	/**
	 * Returns the configuration provider template scope.
	 * 
	 * @return
	 */
	private IConfigurationInstanceProvider getConfigurationProviderTemplateScope() {
		if (configurationProviderTemplateScope == null) {
			configurationProviderTemplateScope = createConfigurationProviderTemplateScope();
		}
		return configurationProviderTemplateScope;
	}

	/**
	 * Create configuration provider template scope.
	 * 
	 * @return
	 */
	private IConfigurationInstanceProvider createConfigurationProviderTemplateScope() {
		try {
			return new ConfigurationInstanceProvider(resource,
					getProjectSettings());
		} catch (CoreException e) {
		}
		return null;
	}

	/**
	 * Set the configuration provider and initialize the scope.
	 */
	public void setConfigurationProvider(
			IConfigurationInstanceProvider configurationProvider) {
		if (configurationProvider == null) {
			setConfigurationProviderScope(SettingsScope.NONE);			
			return;	
		}
		
		switch (configurationProvider.getScope()) {
		case PROJECT:
			setConfigurationProviderScope(SettingsScope.PROJECT);
			this.configurationProviderProjectScope = configurationProvider;
			break;
		case TEMPLATE:
			setConfigurationProviderScope(SettingsScope.TEMPLATE);
			this.configurationProviderTemplateScope = configurationProvider;
			break;
		}
	}

	/**
	 * Create instance provider.
	 */
	@Override
	public IObjectInstanceProvider createProvider(ProviderType providerType, Attributes atts) {
		switch (providerType) {
		case MODEL:
			IModelInstanceProvider modelProvider = getModelProviderTemplateScope();
			setModelProvider(modelProvider);
			return modelProvider;
		case CONFIGURATION:
			IConfigurationInstanceProvider configurationProvider = getConfigurationProviderTemplateScope();
			setConfigurationProvider(configurationProvider);
			return configurationProvider;
		}
		return null;
	}

	@Override
	protected void saveProviderBody(IObjectInstanceProvider instanceProvider,
			Writer writer) throws IOException {
		if (instanceProvider.getScope() == SettingsScope.PROJECT) {
			addAttribute(
					REFERENCE_ID_ATTR,
					((IProjectObjectInstanceProvider) instanceProvider).getId(),
					writer);
		} else {
			addAttribute(CLASS_ATTR, instanceProvider.getClassName(), writer);
			addAttribute(METHOD_ATTR, instanceProvider.getMethodName(), writer);
			addAttribute(TYPE_ATTR, instanceProvider.getType().name(), writer);
		}

	}

	@Override
	protected void saveCustomXMLContent(Writer writer) throws IOException {
		saveProvider(getModelProvider(), writer);
		saveProvider(getConfigurationProvider(), writer);
	}

	@Override
	protected XMLTemplateSettings createXMLSettingsLoader() {
		return new XMLTemplateSettings(this);
	}

	public SettingsScope getScope() {
		return SettingsScope.TEMPLATE;
	}

}
