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
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.ProjectLevelConfigurationSection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;

/**
 * Project level for Model page.
 * 
 */
public class ModelProjectLevelConfigurationSection extends
		ProjectLevelConfigurationSection<ModelPage> {

	public ModelProjectLevelConfigurationSection(ModelPage page,
			Composite parent) {
		super(page, parent);
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);
		// model provider selection has changed from the Table, the Tree of the
		// Data-Model detail must be refreshed.
		getPage().refreshTemplateModels(
				(IModelInstanceProvider) getCurrentInstanceProvider());
	}

	@Override
	protected String getTitle() {
		return FreemarkerUIPluginMessages.ModelProjectLevelConfigurationSection_title;
	}

	@Override
	protected String getDescription() {
		return FreemarkerUIPluginMessages.ModelProjectLevelConfigurationSection_desc;
	}

}
