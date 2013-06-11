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

import java.io.IOException;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.freemarker.launching.FreemarkerLaunchingPlugin;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.LibraryLocation;

/**
 * Freemarker DLTK {@link IInterpreterInstallType} implementation.
 * 
 */
public class GenericFreemarkerInstallType extends
		AbstractInterpreterInstallType {

	private static final String INSTALL_TYPE_NAME = "Generic Freemarker install"; //$NON-NLS-1$

	private static final String[] INTERPRETER_NAMES = { "ftl" }; //$NON-NLS-1$

	private static final LibraryLocation[] EMPTY_LIBRARY_LOCATION = new LibraryLocation[0];

	public String getNatureId() {
		return FreemarkerNature.NATURE_ID;
	}

	public String getName() {
		return INSTALL_TYPE_NAME;
	}

	@Override
	public LibraryLocation[] getDefaultLibraryLocations(
			IFileHandle installLocation, EnvironmentVariable[] variables,
			IProgressMonitor monitor) {
		return EMPTY_LIBRARY_LOCATION;
	}

	protected String getPluginId() {
		return FreemarkerLaunchingPlugin.PLUGIN_ID;
	}

	protected String[] getPossibleInterpreterNames() {
		return INTERPRETER_NAMES;
	}

	protected IInterpreterInstall doCreateInterpreterInstall(String id) {
		return new GenericFreemarkerInstall(this, id);
	}

	public IStatus validateInstallLocation(IFileHandle installLocation) {
		return Status.OK_STATUS;
	}

	protected IPath createPathFile(IDeployment deployment) throws IOException {
		// this method should not be used
		throw new RuntimeException("This method should not be used");
	}

	protected ILog getLog() {
		return FreemarkerLaunchingPlugin.getDefault().getLog();
	}

}
