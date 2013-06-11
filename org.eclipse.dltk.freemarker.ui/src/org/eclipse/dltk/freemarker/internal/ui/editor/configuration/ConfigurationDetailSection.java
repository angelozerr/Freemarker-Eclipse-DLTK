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
import org.eclipse.dltk.freemarker.internal.ui.editor.ClassProviderFormPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.FormLayoutFactory;
import org.eclipse.dltk.freemarker.internal.ui.editor.FreemarkerSection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import freemarker.template.Configuration;

/**
 * Configuration detail section which display the Freemarker
 * {@link Configuration} selected into configuration model provider.
 * 
 */
public class ConfigurationDetailSection extends
		FreemarkerSection<ClassProviderFormPage> {

	public ConfigurationDetailSection(ClassProviderFormPage page,
			Composite parent) {
		super(page, parent, Section.DESCRIPTION);
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
		section.setText(FreemarkerUIPluginMessages.ConfigurationDetailSection_title);
		section
				.setDescription(FreemarkerUIPluginMessages.ConfigurationDetailSection_desc);

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
	}

}
