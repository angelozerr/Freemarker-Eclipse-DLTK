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
package org.eclipse.dltk.freemarker.ui.actions.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerProjectInstaller;

/**
 * Remove Freemarker nature to the selected Eclipse Project.
 * 
 */
public class RemoveFreemarkerNatureActionDelegate extends
		AbstractFreemarkerNatureActionDelegate {

	/**
	 * Remove Freemarker nature to the selected project.
	 * 
	 * @return
	 */
	protected IStatus doRun() {
		IProject project = super.getSelectedProject();
		if (project != null)
			return FreemarkerProjectInstaller.uninstallNature(project);
		return null;
	}

}
