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
package org.eclipse.dltk.freemarker.internal.ui.wizards;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.freemarker.internal.ui.preferences.FreemarkerBuildPathsBlock;
import org.eclipse.dltk.internal.ui.util.CoreUtility;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.dltk.ui.wizards.ProjectWizard;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Wizard to create Freemarker project.
 * 
 */
public class FreemarkerNewProjectWizard extends ProjectWizard {

	private ProjectWizardFirstPage fFirstPage;
	private ProjectWizardSecondPage fSecondPage;

	public static final String WIZARD_ID = "org.eclipse.dltk.freemarker.wizards.newproject"; //$NON-NLS-1$

	public FreemarkerNewProjectWizard() {
		setDefaultPageImageDescriptor(FreemarkerUIPluginImages.DESC_WIZBAN_PROJECT_CREATION);
		setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
		setWindowTitle(FreemarkerWizardMessages.ProjectCreationWizard_title);
	}

	@Override
	public String getScriptNature() {
		return FreemarkerNature.NATURE_ID;
	}

	@Override
	public void addPages() {
		super.addPages();
		fFirstPage = new ProjectWizardFirstPage() {

			@Override
			protected boolean interpeterRequired() {
				return false;
			}
		};
		fFirstPage
				.setTitle(FreemarkerWizardMessages.ProjectCreationWizardFirstPage_title);
		fFirstPage
				.setDescription(FreemarkerWizardMessages.ProjectCreationWizardFirstPage_description);
		addPage(fFirstPage);
		fSecondPage = new ProjectWizardSecondPage(fFirstPage) {
			protected BuildpathsBlock createBuildpathBlock(
					IStatusChangeListener listener) {
				return new FreemarkerBuildPathsBlock(
						new BusyIndicatorRunnableContext(), listener, 0,
						useNewSourcePage(), null);
			}

			public void configureScriptProject(
					IProgressMonitor monitor)
					throws CoreException,
					InterruptedException {
				super.configureScriptProject(monitor);
				
				IProject project = getScriptProject().getProject();
				org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock.addJavaNature(project, monitor);
				
				IFolder folder = project.getFolder("src");
				if (!folder.exists())
					CoreUtility.createFolder(folder, true, true , null);
				
				setClasspath(project);
			};
		};
		addPage(fSecondPage);
	}
	
	private void setClasspath(IProject project) throws JavaModelException, CoreException {
		IJavaProject javaProject = JavaCore.create(project);
		// Set output folder
		//if (data.getOutputFolderName() != null) {
			IPath path = project.getFullPath().append("bin");
			javaProject.setOutputLocation(path, null);
		//}
		IClasspathEntry[] entries = getClassPathEntries(javaProject);
		javaProject.setRawClasspath(entries, null);
	}

	private IClasspathEntry[] getClassPathEntries(IJavaProject project) {
		IClasspathEntry[] internalClassPathEntries = getInternalClassPathEntries(project);
		IClasspathEntry[] entries = new IClasspathEntry[internalClassPathEntries.length + 1];
		System.arraycopy(internalClassPathEntries, 0, entries, 1, internalClassPathEntries.length);

		// Set EE of new project
		String executionEnvironment = null;
		/*if (data instanceof AbstractFieldData) {
			executionEnvironment = ((AbstractFieldData) data).getExecutionEnvironment();
		}*/
		ClasspathComputer.setComplianceOptions(project, executionEnvironment);
		entries[0] = ClasspathComputer.createJREEntry(executionEnvironment);
		//entries[1] = ClasspathComputer.createContainerEntry();

		return entries;
	}

	protected IClasspathEntry[] getInternalClassPathEntries(IJavaProject project) {
//		if (data.getSourceFolderName() == null) {
//			return new IClasspathEntry[0];
//		}
		IClasspathEntry[] entries = new IClasspathEntry[1];
		IPath path = project.getProject().getFullPath().append("src");
		entries[0] = JavaCore.newSourceEntry(path);
		return entries;
	}
}
