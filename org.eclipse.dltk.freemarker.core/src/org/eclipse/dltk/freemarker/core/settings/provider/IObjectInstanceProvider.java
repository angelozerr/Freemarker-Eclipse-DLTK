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

import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.util.StringUtils;

/**
 * Object instance provider used to create instance from the method name of the
 * class name.
 * 
 */
public interface IObjectInstanceProvider {

	/**
	 * Provider type.
	 * 
	 */
	public enum ProviderType {
		MODEL, CONFIGURATION, UNKNOWED;

		public static ProviderType getProviderType(String type) {
			if (StringUtils.isEmpty(type))
				return UNKNOWED;
			if (ProviderType.MODEL.name().equals(type))
				return MODEL;
			if (ProviderType.CONFIGURATION.name().equals(type))
				return CONFIGURATION;
			return UNKNOWED;
		}
	}

	/**
	 * Return the class name.
	 * 
	 * @return
	 */
	String getClassName();

	/**
	 * Set the class name.
	 * 
	 * @param className
	 */
	void setClassName(String className);

	/**
	 * Return the method name.
	 * 
	 * @return
	 */
	String getMethodName();

	/**
	 * Set the method name.
	 * 
	 * @param methodName
	 */
	void setMethodName(String methodName);

	/**
	 * Returns the provider type {@link ProviderType}.
	 * 
	 * @return
	 */
	ProviderType getType();

	/**
	 * Returns the provider scope {@link ProviderScope}.
	 * 
	 * @return
	 */
	SettingsScope getScope();
	
	void reset();
}
