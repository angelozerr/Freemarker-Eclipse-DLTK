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
package org.eclipse.dltk.freemarker.internal.ui.editor;

import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.internal.ui.dialogs.OpenProjectLevelConfigurationDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Abstract class from selection Class+Method provider Page.
 * 
 */
public abstract class ClassProviderFormPage extends FreemarkerFormPage {

	private DescriptionClassProviderSection descriptionSection;
	private TemplateSpecificConfigurationSection specificConfigurationSection;
	protected ProjectLevelConfigurationSection projectConfigurationSection;

	public ClassProviderFormPage(FormEditor editor, String id, String title,
			String titleFormPage, ImageDescriptor imageFormPage) {
		super(editor, id, title, titleFormPage, imageFormPage);
	}

	protected void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));

		// Left panel : Model section
		Composite left = toolkit.createComposite(body);
		left.setLayout(FormLayoutFactory
				.createFormPaneTableWrapLayout(false, 1));
		left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		createLeftPanel(managedForm, left);

		// Right panel
		Composite right = toolkit.createComposite(body);
		right.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false,
				1));
		right.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		createRightPanel(managedForm, right);

		IObjectInstanceProvider provider = getObjectInstanceProvider();
		if (provider != null && provider.getScope() == SettingsScope.PROJECT) {
			descriptionSection.selectProjectLevel();
		} else {
			descriptionSection.selectTemplateSpecific();
		}

	}

	protected void createLeftPanel(IManagedForm managedForm, Composite parent) {
		// Add Section provider description
		descriptionSection = createDescriptionClassProviderSection(parent);
		managedForm.addPart(descriptionSection);

		// Add Section project level
		projectConfigurationSection = createProjectLevelConfigurationSection(parent);
		managedForm.addPart(projectConfigurationSection);

		// Add Section template specific
		specificConfigurationSection = createTemplateSpecificConfigurationSection(parent);
		managedForm.addPart(specificConfigurationSection);
	}

	protected void createRightPanel(IManagedForm managedForm, Composite parent) {

	}

	/**
	 * Create template specific configuration section.
	 * 
	 * @param parent
	 * @return
	 */
	protected abstract TemplateSpecificConfigurationSection createTemplateSpecificConfigurationSection(
			Composite parent);

	/**
	 * Create project level configuration section.
	 * 
	 * @param parent
	 * @return
	 */
	protected abstract ProjectLevelConfigurationSection createProjectLevelConfigurationSection(
			Composite parent);

	/**
	 * Create description configuration section.
	 * 
	 * @param parent
	 * @return
	 */
	protected abstract DescriptionClassProviderSection createDescriptionClassProviderSection(
			Composite parent);

	/**
	 * Return the provider instance.
	 * 
	 * @return
	 */
	protected abstract IObjectInstanceProvider getObjectInstanceProvider();

	protected abstract void setObjectInstanceProvider(
			IObjectInstanceProvider instanceProvider);

	/**
	 * Handler called when a radio selection changed.
	 * 
	 * @param specificConfigurationActivated
	 */
	public void handleRadioChanged(boolean specificConfigurationActivated) {
		specificConfigurationSection.getSection().setEnabled(
				specificConfigurationActivated);
		specificConfigurationSection.getSection().setExpanded(
				specificConfigurationActivated);
		projectConfigurationSection.getSection().setEnabled(
				!specificConfigurationActivated);
		projectConfigurationSection.getSection().setExpanded(
				!specificConfigurationActivated);
	}

	/**
	 * Return the provider type managed by the page.
	 * 
	 * @return
	 */
	public abstract ProviderType getProviderType();

	public boolean refreshProjectProviders() {
		if (projectConfigurationSection == null)
			return false;
		projectConfigurationSection.refreshProjectProviders();
		return true;
	}

	protected abstract void setInstanceProviderScope(SettingsScope scope);

	public void selectNewInstanceProvider(
			OpenProjectLevelConfigurationDialog dialog) {
		if (projectConfigurationSection == null)
			return;
		projectConfigurationSection.selectNewInstanceProvider(dialog);
		descriptionSection.selectProjectLevel();
	}

	@Override
	public void setFocus() {
		// set the default focus
		if (descriptionSection.isProjectLevelSelected()) {
			// project level is selected, set the focus to this section
			projectConfigurationSection.setFocus();
		} else {
			// specific template is selected, set the focus to this section
			specificConfigurationSection.setFocus();
		}
	}
}
