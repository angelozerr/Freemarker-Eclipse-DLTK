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

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;

/**
 * Eclipse Project ClassLoader.
 * 
 */
public interface IClassLoaderProject {

	/**
	 * Return the Eclipse Project.
	 * 
	 * @return
	 */
	IProject getProject();

	/**
	 * Return the Eclipse Project classloader.
	 * 
	 * @return
	 */
	ClassLoader getClassLoader();

	/**
	 * Set the Eclipse Project classloader.
	 * 
	 * @param classLoader
	 */
	void setClassLoader(ClassLoader classLoader);

	/**
	 * Clear the classloader cache.
	 */
	void clearClassLoderCache();

	Object getInstance(String className, String methodName,
			ProviderType type) throws InstanceProviderException;
}
