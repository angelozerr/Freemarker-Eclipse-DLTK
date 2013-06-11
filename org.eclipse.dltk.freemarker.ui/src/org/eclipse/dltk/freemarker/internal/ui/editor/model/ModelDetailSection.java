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
import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;
import org.eclipse.dltk.freemarker.core.util.SettingsUtils;
import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.ClassProviderFormPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.FormLayoutFactory;
import org.eclipse.dltk.freemarker.internal.ui.editor.TreeSection;
import org.eclipse.dltk.freemarker.internal.ui.elements.DefaultContentProvider;
import org.eclipse.dltk.freemarker.internal.ui.parts.TreePart;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import freemarker.provisionnal.ext.ide.TemplateEntriesModel;
import freemarker.provisionnal.ext.ide.TemplateEntryModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Model detail section which display the data-model selected into data model
 * provider.
 * 
 */
public class ModelDetailSection extends TreeSection<ClassProviderFormPage> {

	private static TemplateModel[] EMPTY_TEMPLATE_MODEL_ARRAY = new TemplateModel[0];

	private TreeViewer templateModelTree;

	public ModelDetailSection(ClassProviderFormPage page, Composite parent) {
		super(page, parent, Section.DESCRIPTION, StringUtils.EMPTY_STRING_ARRAY);
	}

	class TemplateModelProvider extends DefaultContentProvider implements
			ITreeContentProvider {
		public Object[] getChildren(Object parent) {
			if (parent instanceof TemplateEntriesModel) {
				try {
				return ((TemplateEntriesModel)parent).entries();
				} catch (TemplateModelException e) {
					handleError(e);
				}
			}
			if (parent instanceof TemplateEntryModel) {
				TemplateEntryModel entry = (TemplateEntryModel)parent;
				if (!entry.isSimpleValue()) {
					return entry.getValues();
				}
			}
			return EMPTY_TEMPLATE_MODEL_ARRAY;
		}

		public boolean hasChildren(Object parent) {
			return getChildren(parent).length > 0;
		}

		public Object getParent(Object child) {
			return null;
		}

		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.internal.ui.editor.FreemarkerSection#createClient
	 * (org.eclipse.ui.forms.widgets.Section,
	 * org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	protected void createClient(Section section, FormToolkit toolkit) {

		section.setLayout(FormLayoutFactory
				.createClearTableWrapLayout(false, 1));

		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);

		Composite container = createClientContainer(section, 1, toolkit);
		TreePart treePart = getTreePart();

		createViewerPartControl(container, SWT.MULTI, 3, toolkit);
		// Set 250 height for the tree detail model
		GridData gridData = (GridData) treePart.getTreeViewer().getTree()
				.getLayoutData();
		gridData.heightHint = 250;

		templateModelTree = treePart.getTreeViewer();
		templateModelTree.setContentProvider(new TemplateModelProvider());
		templateModelTree.setLabelProvider(FreemarkerUIPlugin.getDefault()
				.getTemplateModelLabelProvider());
		toolkit.paintBordersFor(container);
		section.setClient(container);
		// Title + Description Section
		section.setDescription(FreemarkerUIPluginMessages.ModelDetailSection_desc);
		section.setText(FreemarkerUIPluginMessages.ModelDetailSection_title);

		refreshTemplateModels(null);
	}

	/**
	 * Refresh the tree which display the current Data-Model. If modelProvider
	 * is not null, the tree is refreshed with this modelProvider, otherwise it
	 * use the modelProvider coming from the template settings.
	 * 
	 * @param modelProvider
	 */
	public void refreshTemplateModels(IModelInstanceProvider modelProvider) {
		templateModelTree.setInput(getTemplateEntriesModel(modelProvider));
	}

	private TemplateEntriesModel getTemplateEntriesModel(
			IModelInstanceProvider modelProvider) {
		IFreemarkerTemplateSettings templateSettings = getTemplateSettings();
		if (templateSettings != null) {
			try {
				return SettingsUtils.getTemplateEntriesModel(
						modelProvider != null ? modelProvider
								: templateSettings.getModelProvider(),
						templateSettings.getConfigurationProvider());
			} catch (InstanceProviderException e) {
				handleError(e);
			} catch (TemplateModelException e) {
				handleError(e);
			} catch (Throwable e) {
				handleError(e);
			}
		}
		return null;
	}

	private void handleError(Throwable e) {
		// TODO : manage error
		e.printStackTrace();
	}
}
