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
package org.eclipse.dltk.freemarker.internal.ui.editor.model;

import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.TemplateSpecificConfigurationSection;
import org.eclipse.swt.widgets.Composite;

/**
 * Template specific for Model page.
 * 
 */
public class ModelTemplateSpecificConfigurationSection extends
		TemplateSpecificConfigurationSection<ModelPage> {

	public ModelTemplateSpecificConfigurationSection(ModelPage page,
			Composite parent) {
		super(page, parent);
	}

	@Override
	public void refresh() {
		super.refresh();
		// Page is refreshed, the Tree of the Data-Model detail must be
		// refreshed
		// if template settings has model provider with template scope.
		IObjectInstanceProvider instanceProvider = getObjectInstanceProviderTemplateScope(false);
		if (instanceProvider != null) {
			getPage().refreshTemplateModels(
					(IModelInstanceProvider) instanceProvider);
		}
	}

	@Override
	protected String getTitle() {
		return FreemarkerUIPluginMessages.ModelTemplateSpecificConfigurationSection_title;
	}

	@Override
	protected String getDescription() {
		return FreemarkerUIPluginMessages.ModelTemplateSpecificConfigurationSection_desc;
	}
}
