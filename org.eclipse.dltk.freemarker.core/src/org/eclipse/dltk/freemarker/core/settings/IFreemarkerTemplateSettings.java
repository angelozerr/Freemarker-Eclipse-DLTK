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

import org.eclipse.dltk.freemarker.core.settings.provider.IConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;

/**
 * 
 * Template element settings.
 * 
 */
public interface IFreemarkerTemplateSettings extends IFreemarkerElementSettings {

	SettingsScope getModelProviderScope();
	
	void setModelProviderScope(SettingsScope modelProviderScope);
	
	/**
	 * Return the model provider.
	 * 
	 * @return
	 */
	 IModelInstanceProvider getModelProvider();

	/**
	 * Return the model provider.
	 * 
	 * @return
	 */
	//IModelInstanceProvider getTemplateScopeModelProvider();
	
	/**
	 * Set the model provider.
	 * 
	 * @param provider
	 */
	void setModelProvider(IModelInstanceProvider modelProvider);

	SettingsScope getConfigurationProviderScope();
	
	void setConfigurationProviderScope(SettingsScope scope);
	
	/**
	 * Return the configuration provider.
	 * 
	 * @return
	 */
	IConfigurationInstanceProvider getConfigurationProvider();
	
	/**
	 * Set the configuration provider.
	 * 
	 * @param configurationProvider
	 */
	void setConfigurationProvider(
			IConfigurationInstanceProvider configurationProvider);

	
}
