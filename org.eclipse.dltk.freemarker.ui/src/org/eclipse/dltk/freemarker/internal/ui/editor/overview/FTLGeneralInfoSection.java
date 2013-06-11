package org.eclipse.dltk.freemarker.internal.ui.editor.overview;

import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.FormLayoutFactory;
import org.eclipse.dltk.freemarker.internal.ui.editor.FreemarkerFormPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.FreemarkerSection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class FTLGeneralInfoSection extends
		FreemarkerSection<FreemarkerFormPage> {

	public FTLGeneralInfoSection(FreemarkerFormPage page, Composite parent) {
		super(page, parent, Section.DESCRIPTION | SWT.NO_FOCUS| SWT.READ_ONLY);
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
		section.setText(FreemarkerUIPluginMessages.FTLGeneralInfoSection_title);
		section
				.setDescription(FreemarkerUIPluginMessages.FTLGeneralInfoSection_desc);

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
