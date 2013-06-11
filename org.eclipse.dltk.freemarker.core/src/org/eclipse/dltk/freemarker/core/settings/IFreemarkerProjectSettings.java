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

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;

/**
 * Freemarker project settings.
 * 
 */
public interface IFreemarkerProjectSettings extends IFreemarkerElementSettings,
		IClassLoaderProject {

	/**
	 * Return or create '.metatdata/fm' folder from the Eclipse Project.
	 * 
	 * @return
	 * @throws CoreException
	 */
	IFolder getMetadataBaseDir() throws CoreException;

	/**
	 * Return the settings of the Template file.
	 * 
	 * @param file
	 * @return
	 * @throws CoreException
	 */
	IFreemarkerTemplateSettings getTemplateSettings(IFile file)
			throws CoreException;

	/**
	 * Returns list of model providers registered.
	 * 
	 * @return
	 */
	Collection<IProjectObjectInstanceProvider> getModelProviders();

	/**
	 * Returns list of configuration providers registered.
	 * 
	 * @return
	 */
	Collection<IProjectObjectInstanceProvider> getConfigurationProviders();

	/**
	 * Create provider type with the type providerType with
	 * {@link ProviderScope#PROJECT}.
	 * 
	 * @param providerType
	 * @return
	 */
	IProjectObjectInstanceProvider createProvider(ProviderType providerType);

	/**
	 * Remove a provider.
	 * 
	 * @param provider
	 */
	void removeProvider(IProjectObjectInstanceProvider provider);

	/**
	 * Returns the provider instance retrieved with the id and null otherwise.
	 * 
	 * @param id
	 * @return
	 */
	IProjectObjectInstanceProvider getProvider(String id);

	/**
	 * Returns true if name config exist for the provider type and false
	 * otherwise.
	 * 
	 * @param name
	 * @param providerType
	 * @return
	 */
	boolean isNameConfigExist(String name, ProviderType providerType);

}
