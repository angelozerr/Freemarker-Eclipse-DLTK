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
package org.eclipse.dltk.freemarker.internal.launching;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.launching.AbstractRunnableInterpreterRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.RunnableProcess;

/**
 * 
 * Freemarker DLTK {@link IInterpreterRunner} implementation. This class is
 * called with Run As-> Freemarker and execute merge Model with Template (written
 * into Template page from the Freemarker Editor) and display result of the
 * merge into Eclipse console.
 * 
 */
public class FreemarkerInterpreterRunner extends
		AbstractRunnableInterpreterRunner {

	public FreemarkerInterpreterRunner(IInterpreterInstall install) {
		super(install);
	}

	@Override
	protected RunnableProcess createRunnableProcess(ILaunch launch,
			InterpreterConfig config) {
		return new FreemarkerRunnableProcess(super.getInstall(), launch, config);
	}

}
