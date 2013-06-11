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

import java.net.InetAddress;
import java.net.URI;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.freemarker.core.util.SettingsUtils;
import org.eclipse.dltk.launching.DLTKRunnableDebuggingProcess;
import org.eclipse.dltk.launching.DLTKRunnableProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;

import freemarker.provisionnal.debug.impl.dbgp.DbgpDebuggerImpl;
import freemarker.provisionnal.template.ConfigurationProvider;
import freemarker.provisionnal.template.ModelProvider;
import freemarker.template.Configuration;

/**
 * 
 * Freemarker runnable debug process implementation. The
 * {@link DLTKRunnableProcess#run()} method, execute merge Model with Template
 * (written into Template page from the Freemarker Editor) and display result of
 * the merge into Eclipse console.
 * 
 */
public class FreemarkerDebuggerRunnableProcess extends
		DLTKRunnableDebuggingProcess implements ConfigurationProvider,
		ModelProvider {

	public FreemarkerDebuggerRunnableProcess(IInterpreterInstall install,
			ILaunch launch, InterpreterConfig config) {
		super(install, launch, config);
	}

	@Override
	protected IDbgpDebuggerEngine createDbgpDebuggerEngine(
			InetAddress ideAdress, int port, String ideKey, URI fileURI) {
		return new DbgpDebuggerImpl(ideAdress, port, ideKey, fileURI, this,
				this, true);
	}

	public Configuration getConfiguration() {
		return SettingsUtils.getConfiguration(getResourcesFile());
	}

	public Object getModel() {
		return SettingsUtils.getModel(getResourcesFile());
	}

}
