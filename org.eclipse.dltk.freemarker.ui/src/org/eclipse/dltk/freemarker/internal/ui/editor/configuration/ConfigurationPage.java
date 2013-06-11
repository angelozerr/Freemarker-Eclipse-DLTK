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

import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.ClassProviderFormPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.DescriptionClassProviderSection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Configuration page is used to configure :
 * 
 * <ul>
 * <li>Freemarker Configuration: user can customize the {@link Configuration}
 * used when {@link Template#process(Object, java.io.Writer)} is executed in the
 * preview, run and debug action. Run-As, Debug-As....</li>
 * </ul>
 * 
 */
public class ConfigurationPage extends ClassProviderFormPage {

	public static final String PAGE_ID = "configuration";

	private ConfigurationDetailSection configurationDetailSection;

	public ConfigurationPage(FormEditor editor) {
		super(
				editor,
				PAGE_ID,
				FreemarkerUIPluginMessages.FreemarkerEditor_ConfigurationPage_tab,
				FreemarkerUIPluginMessages.FreemarkerEditor_ConfigurationPage_title,
				FreemarkerUIPluginImages.DESC_CONFIGURATION_FM_OBJ);
	}

	@Override
	protected DescriptionClassProviderSection createDescriptionClassProviderSection(
			Composite parent) {
		return new DescriptionConfigurationSection(this, parent);
	}

	@Override
	protected ConfigurationProjectLevelConfigurationSection createProjectLevelConfigurationSection(
			Composite parent) {
		return new ConfigurationProjectLevelConfigurationSection(this, parent);
	}

	@Override
	protected ConfigurationTemplateSpecificConfigurationSection createTemplateSpecificConfigurationSection(
			Composite parent) {
		return new ConfigurationTemplateSpecificConfigurationSection(this,
				parent);
	}

	@Override
	protected IObjectInstanceProvider getObjectInstanceProvider() {
		IFreemarkerTemplateSettings templateSettings = super
				.getTemplateSettings();
		if (templateSettings == null) {
			return null;
		}
		return templateSettings.getConfigurationProvider();
	}

	@Override
	protected void setObjectInstanceProvider(
			IObjectInstanceProvider instanceProvider) {
		IFreemarkerTemplateSettings templateSettings = getTemplateSettings();
		if (templateSettings != null) {
			templateSettings
					.setConfigurationProvider((IConfigurationInstanceProvider) instanceProvider);
		}
	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.CONFIGURATION;
	}

	@Override
	protected void setInstanceProviderScope(SettingsScope scope) {
		IFreemarkerTemplateSettings templateSettings = getTemplateSettings();
		if (templateSettings == null) {
			return;
		}
		templateSettings.setConfigurationProviderScope(scope);
	}

	@Override
	protected void createRightPanel(IManagedForm managedForm, Composite left) {

		// Add Configuration detail section
		configurationDetailSection = createConfigurationDetailSection(left);
		managedForm.addPart(configurationDetailSection);
	}

	private ConfigurationDetailSection createConfigurationDetailSection(
			Composite parent) {
		return new ConfigurationDetailSection(this, parent);
	}

}
