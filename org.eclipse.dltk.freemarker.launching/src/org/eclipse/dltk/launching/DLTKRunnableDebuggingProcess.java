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
package org.eclipse.dltk.launching;

import java.io.File;
import java.net.InetAddress;
import java.net.URI;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.launching.debug.DbgpConnectionConfig;

/**
 * {@link RunnableProcess} with DLTK launching data used to debug.
 * 
 * This class could interest DLTK project?
 */
public abstract class DLTKRunnableDebuggingProcess extends DLTKRunnableProcess {

	private IDbgpDebuggerEngine engine;

	public DLTKRunnableDebuggingProcess(IInterpreterInstall install,
			ILaunch launch, InterpreterConfig config) {
		super(install, launch, config);
	}

	public void run() {
		DbgpConnectionConfig dbgpConfig = DbgpConnectionConfig.load(config);
		try {
			File file = new File(config.getScriptFilePath().toOSString());

			engine = createDbgpDebuggerEngine(InetAddress.getByName(dbgpConfig
					.getHost()), dbgpConfig.getPort(), dbgpConfig
					.getSessionId(), file.toURI());
			engine.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract IDbgpDebuggerEngine createDbgpDebuggerEngine(
			InetAddress ideAdress, int port, String ideKey, URI fileURI);

	@Override
	public void destroy() {
		if (engine != null) {
			engine.stop();
		}
		super.destroy();
	}

}
