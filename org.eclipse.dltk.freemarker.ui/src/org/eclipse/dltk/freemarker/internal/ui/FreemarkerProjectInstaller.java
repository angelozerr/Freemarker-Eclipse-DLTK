package org.eclipse.dltk.freemarker.internal.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.freemarker.internal.ui.util.ProjectUtils;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;

public class FreemarkerProjectInstaller {

	/**
	 * Install Akrogen Nature to the project
	 * 
	 * @param project
	 * @param progressMonitor
	 * @return
	 */
	public static IStatus installNature(IProject project,
			IProgressMonitor progressMonitor) {
		IStatus status = null;
		String natureId = FreemarkerNature.NATURE_ID;
		try {
			// Add Akrogen Nature to the project
			ProjectUtils.addNatureToProject(project, progressMonitor, natureId);
			status = new Status(IStatus.OK, FreemarkerUIPlugin.PLUGIN_ID,
					IStatus.OK, "", null);
		} catch (CoreException e) {
			status = new Status(IStatus.ERROR, FreemarkerUIPlugin.PLUGIN_ID,
					IStatus.OK, "Error adding nature " + natureId
							+ " to project: " + project.getName(), e);
		}

		return status;
	}

	/**
	 * Un-install Akrogen Nature to the project
	 * 
	 * @param project
	 * @return
	 */
	public static IStatus uninstallNature(IProject project) {
		return uninstallNature(project, null);
	}

	/**
	 * Uninstall Akrogen Nature to the project
	 * 
	 * @param project
	 * @param progressMonitor
	 * @return
	 */
	public static IStatus uninstallNature(IProject project,
			IProgressMonitor progressMonitor) {
		IStatus status = null;
		String natureId = FreemarkerNature.NATURE_ID;
		try {
			// Remove Akrogen Nature to the project
			ProjectUtils.removeNatureToProject(project, progressMonitor,
					natureId);
			status = new Status(IStatus.OK, FreemarkerUIPlugin.PLUGIN_ID,
					IStatus.OK, "", null);
		} catch (CoreException e) {
			status = new Status(IStatus.ERROR, FreemarkerUIPlugin.PLUGIN_ID,
					IStatus.OK, "Error remove nature " + natureId
							+ " to project: " + project.getName(), e);
		}

		return status;
	}

}
