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
package org.eclipse.dltk.freemarker.internal.ui.editor.preview;

import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.FreemarkerFormPage;
import org.eclipse.dltk.freemarker.internal.ui.editor.template.TemplateEditor;
import org.eclipse.dltk.freemarker.internal.ui.util.PreviewUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Freemarker Preview page of the FTL template. Preview is the result of merge
 * between models defined and FTL template.
 * 
 */
public class PreviewPage extends FreemarkerFormPage {

	private static final String ID = "preview";

	private StyledText text;

	private Color errorBackgroundColor;
	
	public PreviewPage(FormEditor editor) {
		super(editor, ID,
				FreemarkerUIPluginMessages.FreemarkerEditor_PreviewPage_tab,
				FreemarkerUIPluginMessages.FreemarkerEditor_PreviewPage_title,
				FreemarkerUIPluginImages.DESC_PREVIEW_FM_OBJ);
	}

	protected void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		FillLayout layout = new FillLayout();
		body.setLayout(layout);
		text = new StyledText(body, SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY | SWT.NO_FOCUS);
		text.setEditable(false);
		inititalizeColors();
		PreviewUtils.initialize(text, errorBackgroundColor);
	}
	
	/**
	 * Initialize error colors.
	 */
	private void inititalizeColors() {
		if (getSite().getShell().isDisposed())
			return;
		Display display = getSite().getShell().getDisplay();
		if (display == null || display.isDisposed())
			return;

		errorBackgroundColor = new Color(display, new RGB(245, 203, 206));
	}

	/**
	 * Display the preview of the template editor inti the tab by calling
	 * {@link TemplateEditor#preview(java.io.Writer)};
	 */
	public void preview() {
		PreviewUtils.displayPreview(text, getTemplateEditor());
	}

	protected TemplateEditor getTemplateEditor() {
		return (TemplateEditor) getEditor().getAdapter(TemplateEditor.class);
	}
	
	@Override
	public void dispose() {		
		super.dispose();
		PreviewUtils.dispose(text);
		if (errorBackgroundColor != null) {
			errorBackgroundColor.dispose();
		}
	}

	@Override
	public void setFocus() {
		// Do nothing
	}
}
