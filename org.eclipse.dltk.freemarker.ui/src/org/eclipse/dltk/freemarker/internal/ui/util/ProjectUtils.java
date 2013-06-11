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
package org.eclipse.dltk.freemarker.internal.ui.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ISetSelectionTarget;

/**
 * 
 * Static utility methods for manipulating Eclipse project {@link IProject}.
 *  
 */
public class ProjectUtils {

	/**
	 * Return the {@link IProject}linked to the <code>selection</code>.
	 * 
	 * @param selection
	 * @return
	 */
	public static IProject getActiveProject(ISelection selection) {
		IResource resource = ResourcesUtils.getResource(selection);
		return resource == null ? null : resource.getProject();
	}

	/**
	 * Add nature to the project <code>project</code>.
	 * 
	 * @param project
	 * @param progressMonitor
	 * @param natureId
	 * @throws CoreException
	 */
	public static void addNatureToProject(IProject project,
			IProgressMonitor progressMonitor, String natureId)
			throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();

		int natureIndex = -1;
		for (int i = 0; i < prevNatures.length; i++) {
			if (prevNatures[i].equals(natureId)) {
				natureIndex = i;
				i = prevNatures.length;
			}
		}

		// Add nature only if it is not already there
		if (natureIndex == -1) {
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = natureId;
			description.setNatureIds(newNatures);
			project.setDescription(description, progressMonitor);
		}
	}

	/**
	 * Remove nature to the project <code>project</code>.
	 * 
	 * @param project
	 * @param progressMonitor
	 * @param natureId
	 * @throws CoreException
	 */
	public static void removeNatureToProject(IProject project,
			IProgressMonitor progressMonitor, String natureId)
			throws CoreException {
		IProject proj = project.getProject(); // Needed if project is a
		// IJavaProject
		IProjectDescription description = proj.getDescription();
		String[] prevNatures = description.getNatureIds();

		int natureIndex = -1;
		for (int i = 0; i < prevNatures.length; i++) {
			if (prevNatures[i].equals(natureId)) {
				natureIndex = i;
				i = prevNatures.length;
			}
		}

		// Remove nature only if it exists...
		if (natureIndex != -1) {
			String[] newNatures = new String[prevNatures.length - 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, natureIndex);
			System.arraycopy(prevNatures, natureIndex + 1, newNatures,
					natureIndex, prevNatures.length - (natureIndex + 1));
			description.setNatureIds(newNatures);
			proj.setDescription(description, null);
		}
	}

	/**
	 * 
	 * @param part
	 * @param action
	 * @param selection
	 */
	public static void reselectProject(IWorkbenchPart part, IAction action,
			ISelection selection) {
		if ((part instanceof ISetSelectionTarget) && selection != null)
			((ISetSelectionTarget) part).selectReveal(selection);
	}
}

