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
package org.eclipse.dltk.freemarker.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.launching.AbstractScriptLaunchConfigurationDelegate;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;

/**
 * Freemarker launch configuration delegate.
 * 
 */
public class FreemarkerLaunchConfigurationDelegate extends
		AbstractScriptLaunchConfigurationDelegate {

	protected void runRunner(ILaunchConfiguration configuration,
			IInterpreterRunner runner, InterpreterConfig config,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// if (runner instanceof IConfigurableRunner){
		// IFreemarkerInterpreterRunnerConfig runnerConfig = getConfig();
		// if (runnerConfig!=null){
		// IConfigurableRunner rc=(IConfigurableRunner) runner;
		// rc.setRunnerConfig(runnerConfig);
		// }
		// }
		runner.run(config, launch, monitor);
	}

	// public IFreemarkerInterpreterRunnerConfig getConfig(){
	// return null;
	// }

	public String getLanguageId() {
		return FreemarkerNature.NATURE_ID;
	}
}
