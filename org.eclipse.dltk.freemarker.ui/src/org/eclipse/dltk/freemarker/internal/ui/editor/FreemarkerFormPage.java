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

import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Base class of editor page.
 * 
 */
public abstract class FreemarkerFormPage extends FormPage {

	private Control fLastFocusControl;
	private String titleFormPage;
	private ImageDescriptor imageFormPage;

	public FreemarkerFormPage(FormEditor editor, String id, String title,
			String titleFormPage, ImageDescriptor imageFormPage) {
		super(editor, id, title);
		fLastFocusControl = null;
		this.titleFormPage = titleFormPage;
		this.imageFormPage = imageFormPage;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());

		IToolBarManager manager = form.getToolBarManager();

		getFreemarkerEditor().contributeToToolbar(manager);

		final String href = getHelpResource();
		if (href != null) {
			Action helpAction = new Action("help") { //$NON-NLS-1$
				public void run() {
					BusyIndicator.showWhile(form.getDisplay(), new Runnable() {
						public void run() {
							PlatformUI.getWorkbench().getHelpSystem()
									.displayHelpResource(href);
						}
					});
				}
			};
			helpAction
					.setToolTipText(FreemarkerUIPluginMessages.FreemarkerFormPage_help);
			helpAction.setImageDescriptor(FreemarkerUIPluginImages.DESC_HELP);
			manager.add(helpAction);
		}
		form.setImage(FreemarkerUIPlugin.getDefault().getLabelProvider().get(
				imageFormPage));
		form.setText(titleFormPage);
		fillBody(managedForm, toolkit);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(),
				"TODO");

	}

	
	protected String getHelpResource() {
		return null;
	}

	public FreemarkerMultiPageEditor getFreemarkerEditor() {
		return (FreemarkerMultiPageEditor) getEditor();
	}

	protected IFreemarkerTemplateSettings getTemplateSettings() {
		return (IFreemarkerTemplateSettings) getFreemarkerEditor().getAdapter(
				IFreemarkerTemplateSettings.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createPartControl(org.eclipse.swt
	 * .widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// Dynamically add focus listeners to all the forms children in order
		// to track the last focus control
		IManagedForm managedForm = getManagedForm();
		if (managedForm != null) {
			addLastFocusListeners(managedForm.getForm());
		}
	}

	/**
	 * Programatically and recursively add focus listeners to the specified
	 * composite and its children that track the last control to have focus
	 * before a page change or the editor lost focus
	 * 
	 * @param composite
	 */
	public void addLastFocusListeners(Composite composite) {
		Control[] controls = composite.getChildren();
		for (int i = 0; i < controls.length; i++) {
			Control control = controls[i];
			// Add a focus listener if the control is any one of the below types
			// Note that the controls listed below represent all the controls
			// currently in use by all form pages in PDE. In the future,
			// more controls will have to be added.
			// Could not add super class categories of controls because it
			// would include things like tool bars that we don't want to track
			// focus for.
			if ((control instanceof Text) || (control instanceof Button)
					|| (control instanceof Combo)
					|| (control instanceof CCombo) || (control instanceof Tree)
					|| (control instanceof Table)
					|| (control instanceof Spinner)
					|| (control instanceof Link) || (control instanceof List)
					|| (control instanceof TabFolder)
					|| (control instanceof CTabFolder)
					|| (control instanceof Hyperlink)
					|| (control instanceof FilteredTree)) {
				addLastFocusListener(control);
			}
			if (control instanceof Composite) {
				// Recursively add focus listeners to this composites children
				addLastFocusListeners((Composite) control);
			}
		}
	}

	/**
	 * Add a focus listener to the specified control that tracks the last
	 * control to have focus on this page. When focus is gained by this control,
	 * it registers itself as the last control to have focus. The last control
	 * to have focus is stored in order to be restored after a page change or
	 * editor loses focus.
	 * 
	 * @param control
	 */
	private void addLastFocusListener(final Control control) {
		control.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// NO-OP
			}

			public void focusLost(FocusEvent e) {
				fLastFocusControl = control;
			}
		});
	}

	/**
	 * Set the focus on the last control to have focus before a page change or
	 * the editor lost focus.
	 */
	public void updateFormSelection() {
		if ((fLastFocusControl != null)
				&& (fLastFocusControl.isDisposed() == false)) {
			Control lastControl = fLastFocusControl;
			// Set focus on the control
			lastControl.forceFocus();
			// If the control is a Text widget, select its contents
			if (lastControl instanceof Text) {
				Text text = (Text) lastControl;
				text.setSelection(0, text.getText().length());
			}
		} else {
			// No focus control set
			// Fallback on managed form selection mechanism by setting the
			// focus on this page itself.
			// The managed form will set focus on the first managed part.
			// Most likely this will turn out to be a section.
			// In order for this to work properly, we must override the
			// sections setFocus() method and set focus on a child control
			// (preferrably first) that can practically take focus.
			setFocus();
		}
	}

	public Control getLastFocusControl() {
		return fLastFocusControl;
	}
	
	protected abstract void fillBody(IManagedForm managedForm, FormToolkit toolkit);
}
