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
package org.eclipse.dltk.freemarker.internal.ui.editor.overview;

import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginLabelProvider;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.FormLayoutFactory;
import org.eclipse.dltk.freemarker.internal.ui.editor.FreemarkerFormPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.configuration.ConfigurationPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.model.ModelPage;
import org.eclipse.dltk.freemarker.internal.ui.util.SharedLabelProvider;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Overview Page display general information and explains how configure
 * Model+Configuration provider.
 * 
 */
public class OverviewPage extends FreemarkerFormPage implements
		IHyperlinkListener {

	private static final String ID = "overview";

	public OverviewPage(FormEditor editor) {
		super(editor, ID,
				FreemarkerUIPluginMessages.FreemarkerEditor_OverviewPage_tab,
				FreemarkerUIPluginMessages.FreemarkerEditor_OverviewPage_title,
				FreemarkerUIPluginImages.DESC_OVERVIEW_FM_OBJ);
	}

	protected void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));

		// Left panel
		Composite left = toolkit.createComposite(body);
		left.setLayout(FormLayoutFactory
				.createFormPaneTableWrapLayout(false, 1));
		left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		// General information section
		createGeneralInfoSection(managedForm, left, toolkit);

		// Right panel
		Composite right = toolkit.createComposite(body);
		right.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false,
				1));
		right.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		// Provider section
		createProviderSection(managedForm, right, toolkit);
		// Testing section
		createTestingSection(managedForm, right, toolkit);

	}

	/**
	 * Create General Information section.
	 * 
	 * @param managedForm
	 * @param parent
	 * @param toolkit
	 */
	private void createGeneralInfoSection(IManagedForm managedForm,
			Composite parent, FormToolkit toolkit) {
		FTLGeneralInfoSection infoSection = new FTLGeneralInfoSection(this,
				parent);
		managedForm.addPart(infoSection);
	}

	/**
	 * Create Provider section
	 * 
	 * @param managedForm
	 * @param parent
	 * @param toolkit
	 */
	private void createProviderSection(IManagedForm managedForm,
			Composite parent, FormToolkit toolkit) {
		Section section = createStaticSection(toolkit, parent,
				FreemarkerUIPluginMessages.ProviderSection_title);

		Composite container = createStaticSectionClient(toolkit, section);

		FormText text = createClient(container,
				FreemarkerUIPluginMessages.ProviderSection_desc, toolkit);
		FreemarkerUIPluginLabelProvider lp = FreemarkerUIPlugin.getDefault()
				.getLabelProvider();
		text
				.setImage(
						"page", lp.get(	FreemarkerUIPluginImages.DESC_PAGE_OBJ, SharedLabelProvider.F_EDIT)); //$NON-NLS-1$
		section.setClient(container);
	}

	/**
	 * Create Testing section.
	 * 
	 * @param managedForm
	 * @param parent
	 * @param toolkit
	 */
	private void createTestingSection(IManagedForm managedForm,
			Composite parent, FormToolkit toolkit) {
		Section section = createStaticSection(toolkit, parent,
				FreemarkerUIPluginMessages.TestingSection_title);
		FreemarkerUIPluginLabelProvider lp = FreemarkerUIPlugin.getDefault()
				.getLabelProvider();

		Composite container = createStaticSectionClient(toolkit, section);

		String prefixText = FreemarkerUIPluginMessages.OverviewPage_testing;
		FormText text = createClient(container, getLauncherText(prefixText),
				toolkit);
		text.setImage("run", lp.get(FreemarkerUIPluginImages.DESC_RUN_EXC)); //$NON-NLS-1$
		text.setImage("debug", lp.get(FreemarkerUIPluginImages.DESC_DEBUG_EXC)); //$NON-NLS-1$
		section.setClient(container);
	}

	protected final Section createStaticSection(FormToolkit toolkit,
			Composite parent, String text) {
		Section section = toolkit.createSection(parent,
				ExpandableComposite.TITLE_BAR);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText(text);
		section.setLayout(FormLayoutFactory
				.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);
		return section;
	}

	/**
	 * @param toolkit
	 * @param parent
	 * @return
	 */
	protected Composite createStaticSectionClient(FormToolkit toolkit,
			Composite parent) {
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		container.setLayout(FormLayoutFactory
				.createSectionClientTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		container.setLayoutData(data);
		return container;
	}

	protected final FormText createClient(Composite section, String content,
			FormToolkit toolkit) {
		FormText text = toolkit.createFormText(section, true);
		try {
			text.setText(content, true, false);
		} catch (SWTException e) {
			text.setText(e.getMessage(), false, false);
		}
		text.addHyperlinkListener(this);
		return text;
	}

	protected final String getLauncherText(String message) {

		StringBuffer buffer = new StringBuffer();
		String indent = Short.toString(getIndent());

		// Add run launcher
		addLauncher(buffer, "run", indent, "aa", FreemarkerUIPluginMessages.TestingSection_launchRun);
		// Add debug launcher
		addLauncher(buffer, "debug", indent, "bb",
				FreemarkerUIPluginMessages.TestingSection_launchDebug);
		return NLS.bind(message, buffer.toString());
	}

	private void addLauncher(StringBuffer buffer, String mode, String indent,
			String id, String label) {
		buffer.append("<li style=\"image\" value=\""); //$NON-NLS-1$
		buffer.append(mode);
		buffer
				.append("\" bindent=\"" + indent + "\"><a href=\"launchShortcut."); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(mode);
		buffer.append('.');
		buffer.append(id); //$NON-NLS-1$
		buffer.append("\">"); //$NON-NLS-1$
		buffer.append(label); //$NON-NLS-1$
		buffer.append("</a></li>"); //$NON-NLS-1$
	}

	protected short getIndent() {
		return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.events.HyperlinkListener#linkEntered(org.eclipse
	 * .ui.forms.events.HyperlinkEvent)
	 */
	public void linkEntered(HyperlinkEvent e) {
		IStatusLineManager mng = getEditor().getEditorSite().getActionBars()
				.getStatusLineManager();
		mng.setMessage(e.getLabel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.events.HyperlinkListener#linkExited(org.eclipse.
	 * ui.forms.events.HyperlinkEvent)
	 */
	public void linkExited(HyperlinkEvent e) {
		IStatusLineManager mng = getEditor().getEditorSite().getActionBars()
				.getStatusLineManager();
		mng.setMessage(null);
	}

	public void linkActivated(HyperlinkEvent e) {
		String href = (String) e.getHref();
		if ("launchShortcut.run.aa".equals(href)) {
			// TODO : Implement Run launch
			MessageDialog.openInformation(this.getSite().getShell(), "TODO",
					"Run - TODO");
			return;
		}
		if ("launchShortcut.debug.bb".equals(href)) {
			// TODO : Implement Debug launch
			MessageDialog.openInformation(this.getSite().getShell(), "TODO",
					"Debug - TODO");
			return;
		}
		if (href.equals("configuration_provider")) {
			getEditor().setActivePage(ConfigurationPage.PAGE_ID);
			return;
		}
		if (href.equals("model_provider")) {
			getEditor().setActivePage(ModelPage.PAGE_ID);
			return;
		}
	}
	
	@Override
	public void updateFormSelection() {
		// Disabled the last focus
	}
	
	@Override
	public void setFocus() {
		// Do nothing
	}
}
