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
package org.eclipse.dltk.freemarker.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginLabelProvider;
import org.eclipse.dltk.freemarker.internal.ui.TemplateModelLabelProvider;
import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerTextTools;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FreemarkerUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.dltk.freemarker.ui";

	public static final boolean DUMP_EXCEPTIONS_TO_CONSOLE = Boolean
			.valueOf(
					Platform
							.getDebugOption("org.eclipse.dltk.freemarker.core/dumpErrorsToConsole")) //$NON-NLS-1$
			.booleanValue();

	// The shared instance
	private static FreemarkerUIPlugin plugin;

	private FreemarkerTextTools fTextTools;

	private FreemarkerUIPluginLabelProvider labelProvider;
	private TemplateModelLabelProvider templateModelLabelProvider;

	/**
	 * The constructor
	 */
	public FreemarkerUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static FreemarkerUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Return Freemarker {@link ScriptTextTools}.
	 * 
	 * @return
	 */
	public synchronized FreemarkerTextTools getTextTools() {
		if (fTextTools == null)
			fTextTools = new FreemarkerTextTools(true);
		return fTextTools;
	}

	/**
	 * Log {@link Throwable} error.
	 * 
	 * @param e
	 */
	public static void log(Throwable e) {
		if (DLTKCore.DEBUG || DUMP_EXCEPTIONS_TO_CONSOLE)
			e.printStackTrace();
		String message = e.getMessage();
		if (message == null)
			message = "(no message)"; //$NON-NLS-1$
		getDefault().getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID, 0, message, e));
	}

	public FreemarkerUIPluginLabelProvider getLabelProvider() {
		if (labelProvider == null)
			labelProvider = new FreemarkerUIPluginLabelProvider();
		return labelProvider;
	}

	public TemplateModelLabelProvider getTemplateModelLabelProvider() {
		if (templateModelLabelProvider == null)
			templateModelLabelProvider = new TemplateModelLabelProvider();
		return templateModelLabelProvider;
	}

	
	public static IWorkbenchPage getActivePage() {
		return getDefault().internalGetActivePage();
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	private IWorkbenchPage internalGetActivePage() {
		return getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
}
