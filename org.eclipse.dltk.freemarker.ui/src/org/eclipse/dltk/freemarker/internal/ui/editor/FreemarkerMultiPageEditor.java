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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerSettingsManager;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedEvent;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedListener;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;
import org.eclipse.dltk.freemarker.core.util.SettingsUtils;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.configuration.ConfigurationPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.model.ModelPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.overview.OverviewPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.preview.PreviewPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.template.TemplateEditor;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.FileEditorInput;

import freemarker.provisionnal.template.ConfigurationProvider;
import freemarker.provisionnal.template.ModelProvider;
import freemarker.template.Configuration;

/**
 * Freemarker multi-page editor which contains 3 pages:
 * <ul>
 * <li>page 0 : FTL template editor where user can type FTL template.</li>
 * <li>page 1 : Model page where user can add model of the FTL template. Model
 * is used into ContentAssist when user write FTL template and into Preview
 * page.</li>
 * <li>page 2 : Preview of the FTL template. Preview is the result of merge
 * between models defined and FTL template.</li>
 * </ul>
 * 
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 */
public class FreemarkerMultiPageEditor extends FormEditor implements
		IResourceChangeListener, IFreemarkerSettingsChangedListener,
		ModelProvider, ConfigurationProvider {

	/** Freemarker template editor in page 0. */
	private TemplateEditor freemarkerTemplateEditor;

	private OverviewPage overviewPage;
	private ModelPage modelPage;
	private ConfigurationPage configurationPage;

	/** The text widget used in page 2. */
	private PreviewPage freemarkerPreviewPage;

	private IFile templateFile;

	public FreemarkerMultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void addPages() {
		createTemplatePage();
		// Configuration + Model page must appears ONLY :
		// 1) when the FTL belong to the workspace. Template file is null when
		// user open an FTL wich doesn't belong to the workspace).
		// 2) the FTL file belong to Java Project.
		if (this.templateFile != null) {
			// FTL file belong to the workspace.
			try {
				if (templateFile.getProject().hasNature(JavaCore.NATURE_ID)) {
					// FTL file belong to Java Project.
					createProviderPages();
				}
			} catch (CoreException e) {
				// Do noting
			}
		}
		createPreviewPage();
	}

	private void createProviderPages() {
		createOverviewPage();
		createConfigurationPage();
		createModelPage();
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a Freemarker
	 * template editor.
	 */
	void createTemplatePage() {
		try {
			freemarkerTemplateEditor = new TemplateEditor(this);
			super.addPage(freemarkerTemplateEditor, getEditorInput());
			setPartName(freemarkerTemplateEditor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested template editor page", null,
					e.getStatus());
		}
	}

	@Override
	protected void setPageText(int pageIndex, String text) {
		if (freemarkerTemplateEditor != null
				&& freemarkerTemplateEditor.getIndex() == pageIndex) {
			// Force 'Template' label for the tab name.
			super.setPageText(
					pageIndex,
					FreemarkerUIPluginMessages.FreemarkerEditor_TemplatePage_title);
		} else {
			super.setPageText(pageIndex, text);
		}
	}

	/**
	 * Creates overview of the multi-page editor, which is an overview of the
	 * FTL template.
	 */
	void createOverviewPage() {
		overviewPage = new OverviewPage(this);
		try {
			super.addPage(overviewPage);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested overview page", null, e.getStatus());
		}
	}

	/**
	 * Creates page 1 of the multi-page editor, which contains Configuration
	 * page.
	 */
	void createConfigurationPage() {
		configurationPage = new ConfigurationPage(this);
		try {
			super.addPage(configurationPage);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested configuration page", null,
					e.getStatus());
		}
	}

	/**
	 * Creates page 2 of the multi-page editor, which contains Model page.
	 */
	void createModelPage() {
		modelPage = new ModelPage(this);
		try {
			super.addPage(modelPage);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested model page", null, e.getStatus());
		}
	}

	/**
	 * Creates page 3 of the multi-page editor, which contains Preview page.
	 */
	void createPreviewPage() {
		freemarkerPreviewPage = new PreviewPage(this);
		try {
			super.addPage(freemarkerPreviewPage);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested preview page", null, e.getStatus());
		}
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		commitPages(true);
		freemarkerTemplateEditor.doSave(monitor);
		try {
			IFreemarkerTemplateSettings templateSettings = getTemplateSettings();
			if (templateSettings != null) {
				templateSettings.save();
			}
			refresh();
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR,
					FreemarkerUIPlugin.PLUGIN_ID, 0,
					"Error while saving settings", e);
			ErrorDialog.openError(getSite().getShell(), "Settings error",
					"Error while saving settings", status);
		}
		editorDirtyStateChanged();
	}

	protected void refresh() {
		if (pages != null) {
			for (int i = 0; i < pages.size(); i++) {
				Object page = pages.get(i);
				if (page instanceof IFormPage) {
					IFormPage fpage = (IFormPage) page;
					IManagedForm mform = fpage.getManagedForm();
					if (mform != null)
						mform.refresh();
				}
			}
		}
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IFormPage editor = freemarkerTemplateEditor;
		editor.doSaveAs();
		setPageText(editor.getIndex(), editor.getTitle());
		setInput(editor.getEditorInput());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == freemarkerPreviewPage.getIndex()) {
			// Preview Tab is selected, launch the preview
			preview();
		}
	}

	/**
	 * Preview
	 */
	void preview() {
		freemarkerPreviewPage.preview();
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) freemarkerTemplateEditor
								.getEditorInput()).getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i]
									.findEditor(freemarkerTemplateEditor
											.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == TemplateEditor.class)
			return freemarkerTemplateEditor;
		if (adapter == Configuration.class)
			return getConfiguration();
		if (adapter == IFile.class)
			return templateFile;
		if (adapter == ModelProvider.class) {
			return this;
		}
		if (adapter == ConfigurationProvider.class) {
			return this;
		}
		if (adapter == IFreemarkerTemplateSettings.class) {
			try {
				return getTemplateSettings();
			} catch (CoreException e) {
				return null;
			}
		}
		if (adapter == IFreemarkerProjectSettings.class) {
			try {
				return getTemplateSettings().getProjectSettings();
			} catch (CoreException e) {
				return null;
			}
		}
		return super.getAdapter(adapter);
	}

	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		return new FreemarkerMultiPageEditorSite(this, editor);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		if (input instanceof IFileEditorInput) {
			// Initialize FM Configuration.
			// Set the directory base dir for Freemarker template loading with
			// the directory which contains this file to manage <#import '...'>.
			this.templateFile = ((IFileEditorInput) input).getFile();

			try {
				IFreemarkerTemplateSettings freemarkerTemplate = getTemplateSettings();
				if (freemarkerTemplate != null) {
					freemarkerTemplate.load();
				}
			} catch (CoreException e) {
			}
			try {
				getProjectSettings().addFreemarkerSettingsChangedListener(this);
			} catch (CoreException e) {

			}
		}
	}

	public void contributeToToolbar(IToolBarManager manager) {
		// TODO : add menu to the toolbar
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#setFocus()
	 */
	public void setFocus() {
		super.setFocus();
		IFormPage page = getActivePageInstance();
		// Could be done on setActive in FreemarkerFormPage;
		// but setActive only handles page switches and not focus events
		if ((page != null) && (page instanceof FreemarkerFormPage)) {
			((FreemarkerFormPage) page).updateFormSelection();
		}
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		try {
			getProjectSettings().removeFreemarkerSettingsChangedListener(this);
		} catch (CoreException e) {

		}
		super.dispose();
	}

	/**
	 * Return Freemarker Configuration initialized with base dir of the template
	 * file.
	 * 
	 * @return
	 */
	public Configuration getConfiguration() throws InstanceProviderException {
		return SettingsUtils.getConfiguration(templateFile);
	}

	public Object getModel() throws InstanceProviderException {
		return SettingsUtils.getModel(templateFile);
	}

	public IProject getProject() {
		if (templateFile == null) {
			return null;
		}
		return templateFile.getProject();
	}

	public IFreemarkerProjectSettings getProjectSettings() throws CoreException {
		return FreemarkerSettingsManager.getManager().getProjectSettings(
				getProject());
	}

	public IFreemarkerTemplateSettings getTemplateSettings()
			throws CoreException {
		return FreemarkerSettingsManager.getManager().getTemplateSettings(
				templateFile);
	}

	public void settingsChanged(IFreemarkerSettingsChangedEvent event) {
		switch (event.getType()) {
		case PROJECT_SETTINGS_SAVED:
			configurationPage.refreshProjectProviders();
			modelPage.refreshProjectProviders();
			modelPage.refreshTemplateModels(null);
		}

	}
}
