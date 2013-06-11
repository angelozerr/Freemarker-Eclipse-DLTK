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
package org.eclipse.dltk.freemarker.internal.ui.editor.configuration;

import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.TemplateSpecificConfigurationSection;
import org.eclipse.swt.widgets.Composite;

/**
 * Template specific for Configuration page.
 * 
 */
public class ConfigurationTemplateSpecificConfigurationSection extends
		TemplateSpecificConfigurationSection<ConfigurationPage> {

	public ConfigurationTemplateSpecificConfigurationSection(
			ConfigurationPage page, Composite parent) {
		super(
				page,
				parent);
	}
	
	@Override
	protected String getTitle() {
		return FreemarkerUIPluginMessages.ConfigurationTemplateSpecificConfigurationSection_title;
	}
	
	@Override
	protected String getDescription() {
		return FreemarkerUIPluginMessages.ConfigurationTemplateSpecificConfigurationSection_desc;
	}

}
