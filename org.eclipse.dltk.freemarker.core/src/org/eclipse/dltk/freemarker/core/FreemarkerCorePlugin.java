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
package org.eclipse.dltk.freemarker.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerSettingsManager;
import org.osgi.framework.BundleContext;

import freemarker.log.Logger;

/**
 * The activator class controls the plug-in life cycle
 */
public class FreemarkerCorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.dltk.freemarker.core";

	public static final boolean DUMP_EXCEPTIONS_TO_CONSOLE = Boolean
			.valueOf(
					Platform.getDebugOption("org.eclipse.dltk.freemarker.core/dumpErrorsToConsole")) //$NON-NLS-1$
			.booleanValue();

	// The shared instance
	private static FreemarkerCorePlugin plugin;

	/**
	 * The constructor
	 */
	public FreemarkerCorePlugin() {
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
		// Initialize template/project settings manager.
		FreemarkerSettingsManager.getManager().initialize();
		// Set Freemarker logger to none library (to avoid having
		// ClassNotFoundException SFL4J when Preview View is displayed).
		freemarker.log.Logger.selectLoggerLibrary(Logger.LIBRARY_NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		FreemarkerSettingsManager.getManager().dispose();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static FreemarkerCorePlugin getDefault() {
		return plugin;
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

}
