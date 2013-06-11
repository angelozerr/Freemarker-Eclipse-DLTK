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

import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Description Class provider Section.
 */
public class DescriptionClassProviderSection extends
		FreemarkerSection<ClassProviderFormPage> {

	private String title;
	private String description;

	private Button templateSpecificRadio;
	private Button projectLevelRadio;

	private SelectionListener selectedRadioListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			handleRadioChanged();
		}
	};

	public DescriptionClassProviderSection(ClassProviderFormPage page,
			Composite parent, String title, String description) {
		super(page, parent, Section.DESCRIPTION);
		this.title = title;
		this.description = description;
		createClient(getSection(), page.getEditor().getToolkit());
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
		// Title + Description Section
		if (title != null) {
			section.setText(title);
		}
		if (description != null) {
			section.setDescription(description);
		}

		// Layout section
		section.setLayout(FormLayoutFactory
				.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);

		// Body section
		Composite client = toolkit.createComposite(section);
		client.setLayout(FormLayoutFactory.createSectionClientTableWrapLayout(
				false, 3));
		section.setClient(client);

		// Create radio "Use a project-level configuration"
		projectLevelRadio = createRadio(
				toolkit,
				client,
				FreemarkerUIPluginMessages.ClassSelectionSection_useProjectLevelConfiguration);
		projectLevelRadio.addSelectionListener(selectedRadioListener);

		// Create radio "Use the template specific configuration"
		templateSpecificRadio = createRadio(
				toolkit,
				client,
				FreemarkerUIPluginMessages.ClassSelectionSection_useTheTemplateSpecficConfiguration);
		templateSpecificRadio.addSelectionListener(selectedRadioListener);

		toolkit.paintBordersFor(client);

	}

	/**
	 * Create radio button.
	 * 
	 * @param toolkit
	 * @param parent
	 * @param label
	 * @return
	 */
	private Button createRadio(FormToolkit toolkit, Composite parent,
			String label) {
		Button radio = toolkit.createButton(parent, label, SWT.RADIO);
		TableWrapData layouData = new TableWrapData(TableWrapData.FILL,
				TableWrapData.FILL, 1, 3);
		radio.setLayoutData(layouData);
		return radio;
	}

	@Override
	public void dispose() {
		if (templateSpecificRadio != null && !templateSpecificRadio.isDisposed()) {
			templateSpecificRadio.removeSelectionListener(selectedRadioListener);	
		}
		if (projectLevelRadio != null && !projectLevelRadio.isDisposed()) {
			projectLevelRadio.removeSelectionListener(selectedRadioListener);	
		}
		super.dispose();
	}

	protected void handleRadioChanged() {
		// Radio selection changed.
		boolean selected = templateSpecificRadio.getSelection();
		getPage().handleRadioChanged(selected);
	}

	@Override
	public void setFocus() {
		// By default template specific radio has focus
		// templateSpecificRadio.setFocus();
	}

	public void selectProjectLevel() {
		templateSpecificRadio.setSelection(false);
		projectLevelRadio.setSelection(true);
		handleRadioChanged();
	}

	public void selectTemplateSpecific() {
		templateSpecificRadio.setSelection(true);
		projectLevelRadio.setSelection(false);
		handleRadioChanged();
	}

	@Override
	public void commit(boolean onSave) {
		if (onSave) {
			markStale();
		}
		super.commit(onSave);
	}

	/**
	 * Return true if project level configuration is selected and false
	 * otherwise.
	 * 
	 * @return
	 */
	public boolean isProjectLevelSelected() {
		return projectLevelRadio.getSelection();
	}
}
