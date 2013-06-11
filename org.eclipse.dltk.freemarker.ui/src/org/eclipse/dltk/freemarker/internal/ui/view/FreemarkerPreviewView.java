/*******************************************************************************
 * Copyright (c) 2010 Freemarker Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *   Angelo ZERR                      initial implementation
 *******************************************************************************/
package org.eclipse.dltk.freemarker.internal.ui.view;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedEvent;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedListener;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.editor.template.TemplateEditor;
import org.eclipse.dltk.freemarker.internal.ui.util.PreviewUtils;
import org.eclipse.dltk.freemarker.ui.IFreemarkerUIPluginConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.part.ViewPart;

/**
 * 
 * Freemarker preview View is enable to display the merge between a FTL template
 * and Model at run time. User can type FTL template into the template tab of
 * the Freemarker Editor, an dthis View display the result of merge betwwen
 * Model and Template.
 * 
 */
public class FreemarkerPreviewView extends ViewPart implements
		ISelectionListener, IFreemarkerUIPluginConstants,
		IFreemarkerSettingsChangedListener {

	public static final String VIEW_ID = "org.eclipse.dltk.freemarker.ui.FreemarkerPreviewView";
	/**
	 * result of the Template process
	 */
	private StyledText text = null;

	/**
	 * Progress monitor used to cancel pending computations.
	 * 
	 * @since 3.4
	 */
	private IProgressMonitor fComputeProgressMonitor;

	/** Counts the number of background computation requests. */
	private volatile int fComputeCount;

	private IWorkbenchPart currentFreemarkerEditorPart = null;

	private Color errorBackgroundColor;

	/*
	 * @see IPartListener2
	 */
	private IPartListener2 partListener = new IPartListener2() {
		public void partVisible(IWorkbenchPartReference ref) {
			if (ref.getId().equals(getSite().getId())) {
				IWorkbenchPart activePart = ref.getPage().getActivePart();
				if (activePart != null)
					selectionChanged(activePart, ref.getPage().getSelection());
				startListeningForSelectionChanges();
			}
		}

		public void partHidden(IWorkbenchPartReference ref) {
			if (ref.getId().equals(getSite().getId()))
				stopListeningForSelectionChanges();
		}

		public void partInputChanged(IWorkbenchPartReference ref) {
			if (!ref.getId().equals(getSite().getId()))
				computeAndDoSetInput(ref.getPart(false));
		}

		public void partActivated(IWorkbenchPartReference ref) {
		}

		public void partBroughtToTop(IWorkbenchPartReference ref) {
		}

		public void partClosed(IWorkbenchPartReference ref) {
		}

		public void partDeactivated(IWorkbenchPartReference ref) {
		}

		public void partOpened(IWorkbenchPartReference ref) {
		}
	};

	@Override
	public final void createPartControl(Composite parent) {
		internalCreatePartControl(parent);
		inititalizeColors();
		getSite().getWorkbenchWindow().getPartService()
				.addPartListener(partListener);
		PreviewUtils.initialize(text, errorBackgroundColor);
	}

	protected void internalCreatePartControl(Composite parent) {
		FillLayout layout = new FillLayout();
		parent.setLayout(layout);
		text = new StyledText(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.NO_FOCUS | SWT.READ_ONLY);
		text.setEditable(false);
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

	protected void setForeground(Color color) {
		getControl().setForeground(color);
	}

	protected void setBackground(Color color) {
		getControl().setBackground(color);
	}

	public final void dispose() {
		// cancel possible running computation
		fComputeCount++;
		if (fComputeProgressMonitor != null)
			fComputeProgressMonitor.setCanceled(true);
		getSite().getWorkbenchWindow().getPartService()
				.removePartListener(partListener);
		if (errorBackgroundColor != null) {
			errorBackgroundColor.dispose();
		}
		PreviewUtils.dispose(text);
		removeFreemarkerSettingsChangedListenerIfNeeded();
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part.equals(this))
			return;
		computeAndDoSetInput(part);
	}

	/**
	 * Start to listen for selection changes.
	 */
	protected void startListeningForSelectionChanges() {
		getSite().getPage().addPostSelectionListener(this);
	}

	/**
	 * Stop to listen for selection changes.
	 */
	protected void stopListeningForSelectionChanges() {
		getSite().getPage().removePostSelectionListener(this);
	}

	/**
	 * Determines all necessary details and delegates the computation into a
	 * background thread.
	 * 
	 * @param part
	 *            the workbench part, or <code>null</code> if
	 *            <code>element</code> not <code>null</code>
	 */
	private void computeAndDoSetInput(final IWorkbenchPart part) {
		Assert.isLegal(part != null);

		final int currentCount = ++fComputeCount;
		IWorkbenchPartSite site = part.getSite();
		if (!site.getId().equals(FREEMARKER_EDITOR_ID)) {
			// the current selected part is not a Freemarker editor, display a message.
			PreviewUtils.setMessage(text, FreemarkerUIPluginMessages.FreemarkerPreviewView_notAvailable);			
			removeFreemarkerSettingsChangedListenerIfNeeded();
			return;
		}

		addFreemarkerSettingsChangedListenerIfNeeded(part);
		currentFreemarkerEditorPart = part;
		// the current selected part is a Freemarker editor, get the
		// TemplateEditor.
		final TemplateEditor templateEditor = (TemplateEditor) part
				.getAdapter(TemplateEditor.class);

		if (fComputeProgressMonitor != null)
			fComputeProgressMonitor.setCanceled(true);
		final IProgressMonitor computeProgressMonitor = new NullProgressMonitor();
		fComputeProgressMonitor = computeProgressMonitor;

		Thread thread = new Thread("Freemarker preview input computer") { //$NON-NLS-1$
			public void run() {
				if (currentCount != fComputeCount)
					return;

				Shell shell = getSite().getShell();
				if (shell.isDisposed())
					return;

				Display display = shell.getDisplay();
				if (display.isDisposed())
					return;

				display.asyncExec(new Runnable() {
					/*
					 * @see java.lang.Runnable#run()
					 */
					public void run() {

						if (fComputeCount != currentCount
								|| getViewSite().getShell().isDisposed()) {
							return;
						}
						doSetInput(templateEditor);
						fComputeProgressMonitor = null;
					}
				});
			}
		};

		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();

	}

	private void removeFreemarkerSettingsChangedListenerIfNeeded() {
		if (currentFreemarkerEditorPart != null) {
			// Remove template settings listener changed
			IFreemarkerTemplateSettings templateSettings = (IFreemarkerTemplateSettings) currentFreemarkerEditorPart
					.getAdapter(IFreemarkerTemplateSettings.class);
			if (templateSettings != null) {
				templateSettings.removeFreemarkerSettingsChangedListener(this);
			}
			// Remove project settings listener changed
			IFreemarkerProjectSettings projectSettings = (IFreemarkerProjectSettings) currentFreemarkerEditorPart
					.getAdapter(IFreemarkerProjectSettings.class);
			if (projectSettings != null) {
				projectSettings.removeFreemarkerSettingsChangedListener(this);
			}
			currentFreemarkerEditorPart = null;
		}
	}

	/**
	 * Add listener to observe changed of the template settings. As soon as
	 * template settings, changed, the Preview is refreshed.
	 * 
	 * @param currentPart
	 */
	private void addFreemarkerSettingsChangedListenerIfNeeded(
			IWorkbenchPart currentPart) {
		if (!currentPart.equals(currentFreemarkerEditorPart)) {

			// Remove template+project settings listener changed
			removeFreemarkerSettingsChangedListenerIfNeeded();

			// Add template settings listener changed
			IFreemarkerTemplateSettings templateSettings = (IFreemarkerTemplateSettings) currentPart
					.getAdapter(IFreemarkerTemplateSettings.class);
			if (templateSettings != null) {
				templateSettings.addFreemarkerSettingsChangedListener(this);
			}
			// Add project settings listener changed
			IFreemarkerProjectSettings projectSettings = (IFreemarkerProjectSettings) currentPart
					.getAdapter(IFreemarkerProjectSettings.class);
			if (projectSettings != null) {
				projectSettings.addFreemarkerSettingsChangedListener(this);
			}
		}
	}

	/**
	 * Process the Freemarker preview of the selected template.
	 * 
	 * @param input
	 */
	protected void doSetInput(TemplateEditor input) {
		Shell shell = getSite().getShell();
		if (shell.isDisposed())
			return;

		Display display = shell.getDisplay();
		if (display.isDisposed())
			return;

		PreviewUtils.displayPreview(text, input);
	}

	public void setFocus() {
		// Do nothing
	}

	private Control getControl() {
		return text;
	}

	public void settingsChanged(IFreemarkerSettingsChangedEvent event) {
		if (currentFreemarkerEditorPart != null) {
			computeAndDoSetInput(currentFreemarkerEditorPart);
		}
	}
}
