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
package org.eclipse.dltk.freemarker.core.settings.provider;

/**
 * Object instance provider used to create instance from the method name of the
 * class name used into Project scope.
 * 
 */
public interface IProjectObjectInstanceProvider extends IObjectInstanceProvider {

	public static final IProjectObjectInstanceProvider[] EMPTY_PROVIDER = new IProjectObjectInstanceProvider[0];

	/**
	 * Returns id of the instance provider.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Set the id of the instance provider.
	 * 
	 * @param id
	 */
	void setId(String id);

	/**
	 * Returns name of the instance provider.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Set the name of the instance provider.
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * Returns true if the instance provider is default and false otherwise.
	 * 
	 * @return
	 */
	boolean isDefaultConfig();

	/**
	 * Set if the instance provider is default and false otherwise.
	 * 
	 * @param defaultConfig
	 */
	void setDefaultConfig(boolean defaultConfig);
}
