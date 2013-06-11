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

import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.ClassProviderFormPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.DescriptionClassProviderSection;
import org.eclipse.dltk.freemarker.internal.ui.editor.TemplateSpecificConfigurationSection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * Model page is used to configure :
 * 
 * <ul>
 * <li>Data-Model: user can add model of the FTL template. Model is used into
 * ContentAssist when user write FTL template, into Preview page, Preview View,
 * Run-As, Debug-As....</li>
 * </ul>
 * 
 */
public class ModelPage extends ClassProviderFormPage {

	public static final String PAGE_ID = "model";
	private ModelDetailSection modelDetailSection;

	public ModelPage(FormEditor editor) {
		super(editor, PAGE_ID,
				FreemarkerUIPluginMessages.FreemarkerEditor_ModelPage_tab,
				FreemarkerUIPluginMessages.FreemarkerEditor_ModelPage_title,
				FreemarkerUIPluginImages.DESC_MODEL_FM_OBJ);
	}

	@Override
	protected DescriptionClassProviderSection createDescriptionClassProviderSection(
			Composite parent) {
		return new DescriptionModelSection(this, parent);
	}

	/**
	 * Create project level configuration section.
	 * 
	 * @param parent
	 * @return
	 */
	protected ModelProjectLevelConfigurationSection createProjectLevelConfigurationSection(
			Composite parent) {
		return new ModelProjectLevelConfigurationSection(this, parent);
	}
	
	@Override
	protected TemplateSpecificConfigurationSection createTemplateSpecificConfigurationSection(
			Composite parent) {
		return new ModelTemplateSpecificConfigurationSection(this, parent);
	}

	@Override
	protected IObjectInstanceProvider getObjectInstanceProvider() {
		IFreemarkerTemplateSettings freemarkerTemplate = getTemplateSettings();
		if (freemarkerTemplate == null) {
			return null;
		}
		return freemarkerTemplate.getModelProvider();

	}

	@Override
	protected void setObjectInstanceProvider(
			IObjectInstanceProvider instanceProvider) {
		IFreemarkerTemplateSettings templateSettings = getTemplateSettings();
		if (templateSettings != null) {
			templateSettings
					.setModelProvider((IModelInstanceProvider) instanceProvider);
		}
	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.MODEL;
	}

	@Override
	protected void setInstanceProviderScope(SettingsScope scope) {
		IFreemarkerTemplateSettings freemarkerTemplate = getTemplateSettings();
		if (freemarkerTemplate == null) {
			return;
		}
		freemarkerTemplate.setModelProviderScope(scope);
	}
	
	@Override
	protected void createRightPanel(IManagedForm managedForm, Composite left) {		
		
		// Add Model detail section
		modelDetailSection = createModelDetailSection(left);
		managedForm.addPart(modelDetailSection);
	}
	
	public void refreshTemplateModels(IModelInstanceProvider instanceProvider) {
		if (modelDetailSection == null)
			return;
		modelDetailSection.refreshTemplateModels(instanceProvider);
	}
	
	private ModelDetailSection createModelDetailSection(Composite parent) {
		return new ModelDetailSection(this, parent);
	}
}
