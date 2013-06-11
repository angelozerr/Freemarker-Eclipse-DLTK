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
package org.eclipse.dltk.freemarker.debugger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.freemarker.debug.FreemarkerDebugPlugin;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.RunnableDebuggingEngineRunner;
import org.eclipse.dltk.launching.RunnableProcess;

/**
 * 
 * Freemarker DLTK {@link IInterpreterRunner} implementation. This class is
 * called with Debug As-> Freemarker and execute merge Model with Template
 * (written into Template page from the Freemarker Editor), debug the process of
 * Template and display result of the merge into Eclipse console.
 * 
 */
public class FreemarkerDebuggerRunner extends RunnableDebuggingEngineRunner {

	private static final String TRUE = "true";
	public static final String ENGINE_ID = "org.eclipse.dltk.freemarker.debugger"; //$NON-NLS-1$

	public FreemarkerDebuggerRunner(IInterpreterInstall install) {
		super(install);
	}

	@Override
	protected RunnableProcess createRunnableProcess(ILaunch launch,
			InterpreterConfig config) {
		// Display results of debug into console
		//launch.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT, TRUE);
		return new FreemarkerDebuggerRunnableProcess(super.getInstall(),
				launch, config);
	}

	@Override
	protected InterpreterConfig addEngineConfig(InterpreterConfig config,
			PreferencesLookupDelegate delegate, ILaunch launch)
			throws CoreException {
		return config;
	}

	@Override
	protected String getDebuggingEngineId() {
		return ENGINE_ID;
	}

	@Override
	protected String getDebugPreferenceQualifier() {
		return FreemarkerDebugPlugin.PLUGIN_ID;
	}

	@Override
	protected String getDebuggingEnginePreferenceQualifier() {
		return FreemarkerDebuggerPlugin.PLUGIN_ID;
	}

	@Override
	protected String getLogFileNamePreferenceKey() {
		return null;
	}

}
