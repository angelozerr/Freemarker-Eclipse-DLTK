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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.freemarker.internal.ui.util.ProjectUtils;
import org.eclipse.dltk.freemarker.internal.ui.util.ResourcesUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Abstract class for Action Delegate which is able to :
 * <ul>
 * <li>store the selected selection.</li>
 * <li>store the selected project.</li>
 * <li>store the selected action.</li>
 * <li>store the selected Workbench Part.</li>
 * </ul>
 * 
 * @version 2.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public abstract class AbstractFreemarkerNatureActionDelegate implements
		IWorkbenchWindowActionDelegate {

	private IProject project;

	private IAction action;

	private IWorkbenchPart workbenchPart;

	private ISelection selection;

	/**
	 * Set the action, the selection, and the selected project.
	 * 
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.action = action;
		this.selection = selection;
		// get the project linked to the selection
		this.project = ProjectUtils.getActiveProject(selection);
		// action is enabled only if project is not null and opened
		boolean enabled = (project != null && project.isOpen());
		action.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		// Uninstall Akrogen Nature
		IStatus status = doRun();
		if (status != null && status.getSeverity() == IStatus.ERROR
				&& !status.isOK()) {
			ErrorDialog.openError(null, "", "", status);
			return;
		}
	}
	
	protected abstract IStatus doRun();
	
	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		workbenchPart = targetPart;
	}

	public void init(IWorkbenchWindow iworkbenchwindow) {
		// Do nothing...
	}

	public void dispose() {
		// Do nothing...
	}

	/**
	 * Return the selected file if selection is {@link IFile} otherwise return
	 * null.
	 * 
	 * @return
	 */
	public IFile getSelectedFile() {
		return ResourcesUtils.getFile(selection);
	}

	/**
	 * Return the selected project.
	 * 
	 * @return
	 */
	public IProject getSelectedProject() {
		return project;
	}

	/**
	 * Return the selected workbench part.
	 * 
	 * @return
	 */
	public IWorkbenchPart getSelectedWorkbenchPart() {
		return workbenchPart;
	}

	public ISelection getSelection() {
		return selection;
	}
}
