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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.dltk.freemarker.core.FreemarkerCorePlugin;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.core.util.ClassUtils;
import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.eclipse.dltk.freemarker.internal.core.settings.provider.ProjectConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.internal.core.settings.provider.ProjectModelInstanceProvider;
import org.xml.sax.Attributes;

import freemarker.template.Configuration;

public class FreemarkerProjectSettings extends
		FreemarkerElementSettings<IProject> implements
		IFreemarkerProjectSettings {

	private static final String PROJECT_ELT = "project";

	private static final IPath METDADATA_FOLDER = new Path(".metadata");
	private static final IPath FM_FOLDER = new Path("fm");

	private static final QualifiedName FREEMARKER_PROJECT = new QualifiedName(
			FreemarkerCorePlugin.PLUGIN_ID + ".sessionprops",
			"freemarkerProject");

	private static final QualifiedName FREEMARKER_TEMPLATE = new QualifiedName(
			FreemarkerCorePlugin.PLUGIN_ID + ".sessionprops",
			"freemarkerTemplate");

	private IFolder metadataBaseDir = null;

	private ClassLoader classLoader;

	private List<IProjectObjectInstanceProvider> oldConfigurationProviders = null;
	private List<IProjectObjectInstanceProvider> oldModelProviders = null;

	private List<IProjectObjectInstanceProvider> configurationProviders = null;
	private List<IProjectObjectInstanceProvider> modelProviders = null;

	private boolean loading;

	private Map<String, Object> instancesCache = new HashMap<String, Object>();

	private FreemarkerProjectSettings(IProject project) {
		super(project);
	}

	/**
	 * Create or get Freemarker Project settings.
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IFreemarkerProjectSettings findProjectSettings(
			IProject project) throws CoreException {
		if (project == null) {
			return null;
		}

		IFreemarkerProjectSettings freemarkerProject = (IFreemarkerProjectSettings) project
				.getSessionProperty(FREEMARKER_PROJECT);
		if (freemarkerProject != null) {
			return freemarkerProject;
		}
		freemarkerProject = new FreemarkerProjectSettings(project);
		freemarkerProject.load(true);
		project.setSessionProperty(FREEMARKER_PROJECT, freemarkerProject);
		return freemarkerProject;
	}

	/**
	 * Create or get Freemarker Template settings.
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IFreemarkerTemplateSettings findTemplateSettings(IFile file)
			throws CoreException {
		if (file == null) {
			return null;
		}
		IProject project = file.getProject();
		IFreemarkerProjectSettings freemarkerProject = findProjectSettings(project);
		if (freemarkerProject != null) {
			return freemarkerProject.getTemplateSettings(file);
		}
		return null;
	}

	/**
	 * Create or get Freemarker Template settings.
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public IFreemarkerTemplateSettings getTemplateSettings(IFile file)
			throws CoreException {
		IFreemarkerTemplateSettings freemarkerTemplate = (IFreemarkerTemplateSettings) file
				.getSessionProperty(FREEMARKER_TEMPLATE);
		if (freemarkerTemplate != null) {
			return freemarkerTemplate;
		}
		freemarkerTemplate = new FreemarkerTemplateSettings(file);
		freemarkerTemplate.load(true);
		file.setSessionProperty(FREEMARKER_TEMPLATE, freemarkerTemplate);
		return freemarkerTemplate;
	}

	/**
	 * Return or Create '.fm' folder into the Eclipse Project if needed.
	 * 
	 * @return
	 * @throws CoreException
	 */
	public IFolder getMetadataBaseDir() throws CoreException {
		if (metadataBaseDir != null) {
			return metadataBaseDir;
		}
		// Create '.metdata/fm' folder into the Eclipse Project if needed.
		IProject project = getProject();
		IFolder metadataFolder = project.getFolder(METDADATA_FOLDER);
		if (!metadataFolder.exists()) {
			metadataFolder.create(true, true, null);
		}
		IFolder fmFolder = metadataFolder.getFolder(FM_FOLDER);
		if (!fmFolder.exists()) {
			fmFolder.create(true, true, null);
		}
		metadataBaseDir = fmFolder;
		return metadataBaseDir;
	}

	@Override
	public IFreemarkerProjectSettings getProjectSettings() throws CoreException {
		return this;
	}

	/**
	 * Returns Eclipse project.
	 */
	public IProject getProject() {
		return getResource();
	}

	@Override
	protected String getRootElementName() {
		return PROJECT_ELT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.core.settings.IClassLoaderProject#
	 * clearClassLoderCache()
	 */
	public void clearClassLoderCache() {
		setClassLoader(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.core.settings.IClassLoaderProject#getClassLoader
	 * ()
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.core.settings.IClassLoaderProject#setClassLoader
	 * (java.lang.ClassLoader)
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		instancesCache.clear();
	}

	public IProjectObjectInstanceProvider createProvider(
			ProviderType providerType) {
		return createProvider(providerType, null);
	}

	@Override
	public IProjectObjectInstanceProvider createProvider(
			ProviderType providerType, Attributes atts) {
		IProjectObjectInstanceProvider instanceProvider = null;
		switch (providerType) {
		case MODEL:
			instanceProvider = getOldProvider(oldModelProviders, atts);
			if (instanceProvider == null) {
				instanceProvider = new ProjectModelInstanceProvider(resource,
						this);
			}
			return addProjectInstanceProvider(instanceProvider);
		case CONFIGURATION:
			instanceProvider = getOldProvider(oldConfigurationProviders, atts);
			if (instanceProvider == null) {
				instanceProvider = new ProjectConfigurationInstanceProvider(
						resource, this);
			}
			return addProjectInstanceProvider(instanceProvider);
		}
		return null;
	}

	private IProjectObjectInstanceProvider getOldProvider(
			List<IProjectObjectInstanceProvider> oldInstanceProviders,
			Attributes atts) {
		if (atts == null) {
			return null;
		}
		if (oldInstanceProviders == null) {
			return null;
		}
		String id = atts.getValue(ID_ATTR);
		IProjectObjectInstanceProvider instanceProvider = getProvider(id,
				oldInstanceProviders);
		if (instanceProvider != null) {
			instanceProvider.reset();
			return instanceProvider;
		}
		return null;
	}

	private IProjectObjectInstanceProvider addProjectInstanceProvider(
			IProjectObjectInstanceProvider provider) {
		switch (provider.getType()) {
		case MODEL:
			if (modelProviders == null) {
				modelProviders = new ArrayList<IProjectObjectInstanceProvider>();
			}
			addProjectInstanceProvider(provider, modelProviders);
			break;
		case CONFIGURATION:
			if (configurationProviders == null) {
				configurationProviders = new ArrayList<IProjectObjectInstanceProvider>();
			}
			addProjectInstanceProvider(provider, configurationProviders);
			break;
		}
		return provider;
	}

	private void addProjectInstanceProvider(
			IProjectObjectInstanceProvider provider,
			List<IProjectObjectInstanceProvider> providers) {
		if (!providers.contains(provider)) {
			updateId(provider, providers);
			providers.add(provider);
		}
	}

	private void updateId(IProjectObjectInstanceProvider provider,
			List<IProjectObjectInstanceProvider> providers) {
		if (provider.getId() == null) {
			provider.setId(provider.getType() + "_"
					+ System.currentTimeMillis());
		}
	}

	public void removeProvider(IProjectObjectInstanceProvider provider) {

		switch (provider.getType()) {
		case MODEL:
			removeProjectInstanceProvider(provider, modelProviders);
			break;
		case CONFIGURATION:
			removeProjectInstanceProvider(provider, configurationProviders);
			break;
		}
	}

	private void removeProjectInstanceProvider(
			IProjectObjectInstanceProvider provider,
			List<IProjectObjectInstanceProvider> modelProviders) {
		if (modelProviders != null) {
			modelProviders.remove(provider);
		}
	}

	@Override
	protected void saveCustomXMLContent(Writer writer) throws IOException {
		if (modelProviders != null) {
			for (IProjectObjectInstanceProvider modelProvider : modelProviders) {
				saveProvider(modelProvider, writer);
			}
		}
		if (configurationProviders != null) {
			for (IProjectObjectInstanceProvider configurationProvider : configurationProviders) {
				saveProvider(configurationProvider, writer);
			}
		}
	}

	@Override
	protected void saveProviderBody(IObjectInstanceProvider instanceProvider,
			Writer writer) throws IOException {
		IProjectObjectInstanceProvider projectProvider = (IProjectObjectInstanceProvider) instanceProvider;
		addAttribute(ID_ATTR, projectProvider.getId(), writer);
		addAttribute(NAME_ATTR, projectProvider.getName(), writer);
		addAttribute(DEFAULT_ATTR, projectProvider.isDefaultConfig(), writer);
		addAttribute(CLASS_ATTR, instanceProvider.getClassName(), writer);
		addAttribute(METHOD_ATTR, instanceProvider.getMethodName(), writer);
		addAttribute(TYPE_ATTR, instanceProvider.getType().name(), writer);
	}

	public Collection<IProjectObjectInstanceProvider> getModelProviders() {
		if (modelProviders == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(modelProviders);
	}

	public Collection<IProjectObjectInstanceProvider> getConfigurationProviders() {
		if (configurationProviders == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(configurationProviders);
	}

	@Override
	protected XMLProjectSettingsLoader createXMLSettingsLoader() {
		return new XMLProjectSettingsLoader(this);
	}

	@Override
	public void load(boolean force) throws CoreException {
		try {
			loading = true;
			oldModelProviders = modelProviders;
			oldConfigurationProviders = configurationProviders;
			modelProviders = null;
			configurationProviders = null;
			super.load(force);
			oldModelProviders = modelProviders;
			oldConfigurationProviders = configurationProviders;
		} finally {
			loading = false;
		}
	}

	public boolean isLoading() {
		return loading;
	}

	public IProjectObjectInstanceProvider getProvider(String id) {
		if (id == null)
			return null;
		IProjectObjectInstanceProvider provider = getProvider(id,
				modelProviders);
		if (provider != null) {
			return provider;
		}
		return getProvider(id, configurationProviders);
	}

	private IProjectObjectInstanceProvider getProvider(String id,
			List<IProjectObjectInstanceProvider> providers) {
		if (providers == null)
			return null;
		for (IProjectObjectInstanceProvider provider : providers) {
			if (id.equals(provider.getId()))
				return provider;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.core.settings.IFreemarkerElementSettings#
	 * getScope()
	 */
	public SettingsScope getScope() {
		return SettingsScope.PROJECT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings#
	 * isNameConfigExist(java.lang.String,
	 * org.eclipse.dltk.freemarker.core.settings
	 * .provider.IObjectInstanceProvider.ProviderType)
	 */
	public boolean isNameConfigExist(String name, ProviderType providerType) {
		switch (providerType) {
		case MODEL:
			return isNameConfigExist(name, modelProviders);
		case CONFIGURATION:
			return isNameConfigExist(name, configurationProviders);
		}
		return false;
	}

	private boolean isNameConfigExist(String name,
			List<IProjectObjectInstanceProvider> providers) {
		if (providers == null)
			return false;
		for (IProjectObjectInstanceProvider provider : providers) {
			if (name.equals(provider.getName()))
				return true;
		}
		return false;
	}

	/**
	 * Returns the instance from the className#methodName and by checking
	 * the type returned switch the providerType
	 */
	public Object getInstance(String className, String methodName,
			ProviderType providerType) throws InstanceProviderException {
		if (StringUtils.isEmpty(className) || StringUtils.isEmpty(methodName)) {
			return null;
		}
		String key = className + "_" + methodName + "_" + providerType.name();
		Object instanceCache = instancesCache.get(key);
		if (instanceCache != null) {
			return instanceCache;
		}
		try {
			// Create the instance
			Object instance = ClassUtils.execute(this, className, methodName);
			// Test if type returned is OK
			switch (providerType) {
			case CONFIGURATION:
				if (!(instance instanceof Configuration)) {
					throw new InstanceProviderException(providerType, className
							+ "#" + methodName + "() must returns "
							+ Configuration.class);
				}
				break;
			}
			// Instance is valid, cache it and return it.
			if (providerType == ProviderType.MODEL) {
				// For the model provider, the instance must be wrapped with InstanceModelCache
				instanceCache = new InstanceModelCache(instance);	
			}
			else {
				instanceCache = instance;
			}			
			instancesCache.put(key, instanceCache);
			return instanceCache;
		} catch (SecurityException e) {
			throw new InstanceProviderException(providerType, e);
		} catch (IllegalArgumentException e) {
			throw new InstanceProviderException(providerType, e);
		} catch (CoreException e) {
			throw new InstanceProviderException(providerType, e);
		} catch (ClassNotFoundException e) {
			throw new InstanceProviderException(providerType, e);
		} catch (InstantiationException e) {
			throw new InstanceProviderException(providerType, e);
		} catch (IllegalAccessException e) {
			throw new InstanceProviderException(providerType, e);
		} catch (NoSuchMethodException e) {
			throw new InstanceProviderException(providerType, e);
		} catch (InvocationTargetException e) {
			throw new InstanceProviderException(providerType, e);
		}
	}

}
