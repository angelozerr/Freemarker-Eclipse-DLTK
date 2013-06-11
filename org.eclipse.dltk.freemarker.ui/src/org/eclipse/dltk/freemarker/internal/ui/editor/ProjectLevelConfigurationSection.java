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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.dialogs.OpenProjectLevelConfigurationDialog;
import org.eclipse.dltk.freemarker.internal.ui.elements.DefaultContentProvider;
import org.eclipse.dltk.freemarker.internal.ui.jdt.FreemarkerJavaHelperUI;
import org.eclipse.dltk.freemarker.internal.ui.parts.EditableTablePart;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Project level configuration section used to select a configuration/model
 * provider for the whole templates of the Eclipse project.
 * 
 */
public abstract class ProjectLevelConfigurationSection<T extends ClassProviderFormPage>
		extends TableSection<T> {

	private static final int ADD_BUTTON_INDEX = 0;
	private static final int EDIT_BUTTON_INDEX = 1;
	private static final int REMOVE_BUTTON_INDEX = 2;
	private static final int DUPLICATE_BUTTON_INDEX = 3;
	private static final int OPEN_CLASS_BUTTON_INDEX = 4;

	class ProjectObjectInstanceContentProvider extends DefaultContentProvider
			implements IStructuredContentProvider {

		public Object[] getElements(Object parent) {
			IFreemarkerProjectSettings projectSettings = (IFreemarkerProjectSettings) parent;
			if (projectSettings != null) {
				ProviderType providerType = getPage().getProviderType();
				switch (providerType) {
				case MODEL:
					return projectSettings.getModelProviders().toArray(
							IProjectObjectInstanceProvider.EMPTY_PROVIDER);
				case CONFIGURATION:
					return projectSettings.getConfigurationProviders().toArray(
							IProjectObjectInstanceProvider.EMPTY_PROVIDER);
				}
			}
			return IProjectObjectInstanceProvider.EMPTY_PROVIDER;
		}
	}

	private TableViewer fProjectObjectInstanceViewer;
	private IObjectInstanceProvider currentInstanceProvider;

	public ProjectLevelConfigurationSection(T page, Composite parent) {
		super(
				page,
				parent,
				Section.DESCRIPTION | Section.EXPANDED | Section.TWISTIE,
				new String[] {
						FreemarkerUIPluginMessages.ProjectLevelConfigurationSection_addButton,
						FreemarkerUIPluginMessages.ProjectLevelConfigurationSection_editButton,
						FreemarkerUIPluginMessages.ProjectLevelConfigurationSection_removeButton,
						FreemarkerUIPluginMessages.ProjectLevelConfigurationSection_duplicateButton,
						FreemarkerUIPluginMessages.ProjectLevelConfigurationSection_openClassButton });
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setDescription(getDescription());
		section.setText(getTitle());

		section.setLayout(FormLayoutFactory
				.createClearTableWrapLayout(false, 1));

		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);

		Composite container = createClientContainer(section, 2, toolkit);
		EditableTablePart tablePart = getTablePart();
		tablePart.setEditable(isEditable());

		createViewerPartControl(container, SWT.FULL_SELECTION, 2, toolkit);
		fProjectObjectInstanceViewer = tablePart.getTableViewer();
		fProjectObjectInstanceViewer
				.setContentProvider(new ProjectObjectInstanceContentProvider());
		fProjectObjectInstanceViewer.setLabelProvider(FreemarkerUIPlugin
				.getDefault().getLabelProvider());

		fProjectObjectInstanceViewer.setInput(getProjectSettings());

		toolkit.paintBordersFor(container);
		section.setClient(container);
	}

	@Override
	protected void buttonSelected(int index) {
		switch (index) {
		case ADD_BUTTON_INDEX:
			handleAdd();
			break;
		case EDIT_BUTTON_INDEX:
			handleEdit();
			break;
		case REMOVE_BUTTON_INDEX:
			handleRemove();
			break;
		case DUPLICATE_BUTTON_INDEX:
			handleDuplicate();
			break;
		case OPEN_CLASS_BUTTON_INDEX:
			handleOpenClass();
			break;

		}
	}

	/**
	 * Add button clicked.
	 */
	private void handleAdd() {
		// Open the dialog which save the provider project settings.
		BusyIndicator.showWhile(getSection().getDisplay(), new Runnable() {
			public void run() {
				OpenProjectLevelConfigurationDialog dialog;
				try {
					dialog = new OpenProjectLevelConfigurationDialog(
							getSection().getShell(), getTemplateSettings()
									.getProjectSettings(), null, null,
							getPage().getProviderType());

					dialog.create();
					if (dialog.open() == Window.OK) {
						selectNewInstanceProvider(dialog);
					}
				} catch (CoreException e) {
					IStatus status = new Status(IStatus.ERROR,
							FreemarkerUIPlugin.PLUGIN_ID, 0,
							"Error while loading project settings", e);
					ErrorDialog.openError(getSection().getShell(),
							"Settings error",
							"Error while loading project settings", status);

				}
			}
		});
	}

	/**
	 * Edit button clicked.
	 */
	private void handleEdit() {
		final IProjectObjectInstanceProvider instanceProvider = getSelectedInstanceProvider();
		if (instanceProvider == null)
			return;
		// Open the dialog which save the provider project settings.
		BusyIndicator.showWhile(getSection().getDisplay(), new Runnable() {
			public void run() {
				OpenProjectLevelConfigurationDialog dialog;
				try {
					dialog = new OpenProjectLevelConfigurationDialog(
							getSection().getShell(), getTemplateSettings()
									.getProjectSettings(), instanceProvider);

					dialog.create();
					if (dialog.open() == Window.OK) {

					}
				} catch (CoreException e) {
					IStatus status = new Status(IStatus.ERROR,
							FreemarkerUIPlugin.PLUGIN_ID, 0,
							"Error while loading project settings", e);
					ErrorDialog.openError(getSection().getShell(),
							"Settings error",
							"Error while loading project settings", status);

				}
			}
		});
	}

	/**
	 * Remove button clicked.
	 */
	private void handleRemove() {
		IProjectObjectInstanceProvider instanceProvider = getSelectedInstanceProvider();
		if (instanceProvider == null)
			return;
		IFreemarkerProjectSettings projectSettings = getProjectSettings();
		if (projectSettings != null) {
			projectSettings.removeProvider(instanceProvider);
			try {
				projectSettings.save();
			} catch (CoreException e) {
				IStatus status = new Status(IStatus.ERROR,
						FreemarkerUIPlugin.PLUGIN_ID, 0,
						"Error while saving project settings", e);
				ErrorDialog.openError(getSection().getShell(),
						"Settings error",
						"Error while saving project settings", status);
			}
		}

	}

	/**
	 * Add Open Class button click.
	 */
	private void handleOpenClass() {
		doOpenJavaEditor();
	}

	/**
	 * Duplicate button clicked.
	 */
	private void handleDuplicate() {
		final IProjectObjectInstanceProvider instanceProvider = getSelectedInstanceProvider();
		if (instanceProvider == null)
			return;
		// Open the dialog which save the provider project settings.
		BusyIndicator.showWhile(getSection().getDisplay(), new Runnable() {
			public void run() {
				OpenProjectLevelConfigurationDialog dialog;
				try {
					dialog = new OpenProjectLevelConfigurationDialog(
							getSection().getShell(), getTemplateSettings()
									.getProjectSettings(), instanceProvider
									.getClassName(), instanceProvider
									.getMethodName(), instanceProvider
									.getType());

					dialog.create();
					if (dialog.open() == Window.OK) {
						selectNewInstanceProvider(dialog);
					}
				} catch (CoreException e) {
					IStatus status = new Status(IStatus.ERROR,
							FreemarkerUIPlugin.PLUGIN_ID, 0,
							"Error while loading project settings", e);
					ErrorDialog.openError(getSection().getShell(),
							"Settings error",
							"Error while loading project settings", status);

				}
			}
		});
	}

	public void refreshProjectProviders() {
		fProjectObjectInstanceViewer.refresh();
	}

	@Override
	public void commit(boolean onSave) {
		if (onSave) {
			if (getSection().isExpanded()) {
				getPage().setObjectInstanceProvider(currentInstanceProvider);
			}
		}
		super.commit(onSave);
	}

	@Override
	public void refresh() {
		IObjectInstanceProvider instanceProvider = getPage()
				.getObjectInstanceProvider();
		if (instanceProvider != null
				&& instanceProvider.getScope() == SettingsScope.PROJECT) {
			selectInstanceProvider(instanceProvider);
		}
		setEnabledButtons(currentInstanceProvider != null);
		super.refresh();
	}

	private void selectInstanceProvider(IObjectInstanceProvider instanceProvider) {
		currentInstanceProvider = instanceProvider;
		fProjectObjectInstanceViewer.setSelection(new StructuredSelection(
				instanceProvider), true);
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		IObjectInstanceProvider selectedInstanceProvider = getSelectedInstanceProvider();
		if (currentInstanceProvider == null
				|| !currentInstanceProvider.equals(selectedInstanceProvider)) {
			// When instance provider is selected, page must be saved.
			super.fireSaveNeeded();
		}
		currentInstanceProvider = selectedInstanceProvider;
		setEnabledButtons(currentInstanceProvider != null);
	}

	private void setEnabledButtons(boolean enabled) {
		getTablePart().getButton(EDIT_BUTTON_INDEX).setEnabled(enabled);
		getTablePart().getButton(REMOVE_BUTTON_INDEX).setEnabled(enabled);
		getTablePart().getButton(DUPLICATE_BUTTON_INDEX).setEnabled(enabled);
		getTablePart().getButton(OPEN_CLASS_BUTTON_INDEX).setEnabled(enabled);
	}

	private IProjectObjectInstanceProvider getSelectedInstanceProvider() {
		ISelection selection = fProjectObjectInstanceViewer.getSelection();
		if (selection.isEmpty())
			return null;
		return (IProjectObjectInstanceProvider) ((IStructuredSelection) selection)
				.getFirstElement();
	}

	@Override
	protected void handleDoubleClick(IStructuredSelection selection) {
		doOpenJavaEditor();
	}

	/**
	 * Open the Java Editor with class/method nom of the selected instance
	 * provider.
	 */
	private void doOpenJavaEditor() {
		// doucle click on selected provider, open the Java Class Editor.
		IProject project = getProject();
		if (project == null)
			return;

		IProjectObjectInstanceProvider instanceProvider = getSelectedInstanceProvider();
		if (instanceProvider == null)
			return;

		String className = instanceProvider.getClassName();
		String methodName = instanceProvider.getMethodName();

		FreemarkerJavaHelperUI.createClass(className, methodName, project,
				null, false);
	}

	/**
	 * Get the instance provider created by the dialog and update the selection
	 * + teh settings.
	 * 
	 * @param dialog
	 */
	public void selectNewInstanceProvider(
			OpenProjectLevelConfigurationDialog dialog) {
		IProjectObjectInstanceProvider projectProvider = dialog
				.getProjectInstanceProvider();
		if (projectProvider != null) {
			// Select into the list of provider, the instance provider created
			selectInstanceProvider(projectProvider);
			// Update the template settings
			getPage().setObjectInstanceProvider(projectProvider);
			// Set the dirty editor
			super.fireSaveNeeded();
		}
	}

	public IObjectInstanceProvider getCurrentInstanceProvider() {
		return currentInstanceProvider;
	}

	@Override
	public void setFocus() {
		if (currentInstanceProvider != null) {
			super.setFocus();
		}
	}
	
	@Override
	protected boolean createCount() {
		// Display Total line
		return true;
	}
	
	protected abstract String getTitle();

	protected abstract String getDescription();

}
