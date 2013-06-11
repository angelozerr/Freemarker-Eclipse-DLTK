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
package org.eclipse.dltk.freemarker.internal.ui.jdt;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.freemarker.core.jdt.FreemarkerJavaHelper;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.jdt.contentassist.TypeContentProposalListener;
import org.eclipse.dltk.freemarker.internal.ui.jdt.contentassist.TypeContentProposalProvider;
import org.eclipse.dltk.freemarker.internal.ui.jdt.contentassist.TypeFieldAssistDisposer;
import org.eclipse.dltk.freemarker.internal.ui.jdt.contentassist.TypeProposalLabelProvider;
import org.eclipse.dltk.freemarker.internal.ui.parts.ConditionalListSelectionDialog;
import org.eclipse.dltk.freemarker.internal.ui.util.SWTUtils;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

public class FreemarkerJavaHelperUI {

	public static String selectType(IResource resource, int scope) {
		if (resource == null)
			return null;
		IProject project = resource.getProject();
		try {
			SelectionDialog dialog = JavaUI.createTypeDialog(FreemarkerUIPlugin
					.getActiveWorkbenchShell(), PlatformUI.getWorkbench()
					.getProgressService(), FreemarkerJavaHelper
					.getSearchScope(project), scope, false, ""); //$NON-NLS-1$
			dialog
					.setTitle(FreemarkerUIPluginMessages.ClassAttributeRow_dialogTitle);
			if (dialog.open() == Window.OK) {
				IType type = (IType) dialog.getResult()[0];
				return type.getFullyQualifiedName('$');
			}
		} catch (JavaModelException e) {
		}
		return null;
	}

	public static IType selectType(IResource resource, int scope,
			String filter, String superTypeName) {
		if (resource == null)
			return null;
		IProject project = resource.getProject();
		try {
			IJavaSearchScope searchScope = null;
			if (superTypeName != null
					&& !superTypeName.equals("java.lang.Object")) { //$NON-NLS-1$
				IJavaProject javaProject = JavaCore.create(project);
				IType superType = javaProject.findType(superTypeName);
				if (superType != null)
					searchScope = SearchEngine.createHierarchyScope(superType);
			}
			if (searchScope == null)
				searchScope = FreemarkerJavaHelper.getSearchScope(project);

			SelectionDialog dialog = JavaUI.createTypeDialog(FreemarkerUIPlugin
					.getActiveWorkbenchShell(), PlatformUI.getWorkbench()
					.getProgressService(), searchScope, scope, false, filter);
			dialog
					.setTitle(FreemarkerUIPluginMessages.ClassAttributeRow_dialogTitle);
			if (dialog.open() == Window.OK) {
				return (IType) dialog.getResult()[0];
				//return type.getFullyQualifiedName('$');
			}
		} catch (JavaModelException e) {
		}
		return null;
	}

	/**
	 * Open/Create a java class
	 * 
	 * @param name
	 *            fully qualified java classname
	 * @param project
	 * @param value
	 *            for creation of the class
	 * @param createIfNoNature
	 *            will create the class even if the project has no java nature
	 * @return null if the class exists or the name of the newly created class
	 */
	public static String createClass(String name, String methodName, IProject project,
			Object value, boolean createIfNoNature) {
		// name = TextUtil.trimNonAlphaChars(name).replace('$', '.');
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(project);
				IJavaElement result = null;
				if (name.length() > 0)
					result = javaProject.findType(name);				
				if (methodName != null) {
					result = ((IType)result).getMethod(methodName, new String[0]);
				}
				if (result != null)
					JavaUI.openInEditor(result);
				else {
					// JavaAttributeWizard wizard = new
					// JavaAttributeWizard(value);
					// WizardDialog dialog = new
					// WizardDialog(FreemarkerUIPlugin.getActiveWorkbenchShell(),
					// wizard);
					// dialog.create();
					// SWTUtil.setDialogSize(dialog, 400, 500);
					// int dResult = dialog.open();
					// if (dResult == Window.OK)
					// return wizard.getQualifiedNameWithArgs();
				}
			} else if (createIfNoNature) {
				IResource resource = project.findMember(new Path(name));
				if (resource != null && resource instanceof IFile) {
					// IWorkbenchPage page = FreemarkerUIPlugin.getActivePage();
					// IDE.openEditor(page, (IFile) resource, true);
				} else {
					// JavaAttributeWizard wizard = new
					// JavaAttributeWizard(value);
					// WizardDialog dialog = new
					// WizardDialog(FreemarkerUIPlugin.getActiveWorkbenchShell(),
					// wizard);
					// dialog.create();
					// SWTUtil.setDialogSize(dialog, 400, 500);
					// int dResult = dialog.open();
					// if (dResult == Window.OK) {
					// String newValue = wizard.getQualifiedName();
					//						name = newValue.replace('.', '/') + ".java"; //$NON-NLS-1$
					// resource = project.findMember(new Path(name));
					// if (resource != null && resource instanceof IFile) {
					// IWorkbenchPage page = FreemarkerUIPlugin.getActivePage();
					// IDE.openEditor(page, (IFile) resource, true);
					// }
					// return newValue;
					// }
				}
			}
		} catch (PartInitException e) {
			FreemarkerUIPlugin.log(e);
		} catch (JavaModelException e) {
			// nothing
			Display.getCurrent().beep();
		} catch (CoreException e) {
			FreemarkerUIPlugin.log(e);
		}
		return null;
	}

	/**
	 * Disposer returned used to dispose of label provider and remove listeners
	 * Callers responsibility to call dispose method when underlying text widget
	 * is being disposed
	 * 
	 * @param text
	 * @param project
	 */
	public static TypeFieldAssistDisposer addTypeFieldAssistToText(Text text,
			IProject project, int searchScope) {
		// Decorate the text widget with the light-bulb image denoting content
		// assist
		int bits = SWT.TOP | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(text, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		controlDecoration
				.setDescriptionText(FreemarkerUIPluginMessages.FreemarkerJavaHelper_msgContentAssistAvailable);
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry
				.getDefault().getFieldDecoration(
						FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());

		// Create the proposal provider
		TypeContentProposalProvider proposalProvider = new TypeContentProposalProvider(
				project, searchScope);
		// Default text widget adapter for field assist
		TextContentAdapter textContentAdapter = new TextContentAdapter();
		// Set auto activation character to be a '.'
		char[] autoActivationChars = new char[] { TypeContentProposalProvider.F_DOT };
		// Create the adapter
		ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(
				text, textContentAdapter, proposalProvider,
				IWorkbenchCommandConstants.EDIT_CONTENT_ASSIST,
				autoActivationChars);
		// Configure the adapter
		// Add label provider
		ILabelProvider labelProvider = new TypeProposalLabelProvider();
		adapter.setLabelProvider(labelProvider);
		// Replace text field contents with accepted proposals
		adapter
				.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		// Disable default filtering - custom filtering done
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		// Add listeners required to reset state for custom filtering
		TypeContentProposalListener proposalListener = new TypeContentProposalListener();
		adapter
				.addContentProposalListener((IContentProposalListener) proposalListener);
		adapter
				.addContentProposalListener((IContentProposalListener2) proposalListener);

		return new TypeFieldAssistDisposer(adapter, proposalListener);
	}

	public static IMethod handleAdd(final IProject project, final String className, final String methodName) {
		try {
			if (!project.hasNature(JavaCore.NATURE_ID)) {
				return null;
			}

			ILabelProvider labelProvider = new JavaElementLabelProvider();
			final ConditionalListSelectionDialog dialog = new ConditionalListSelectionDialog(
					FreemarkerUIPlugin.getActiveWorkbenchShell(),
					labelProvider,
					"");

			Runnable runnable = new Runnable() {
				public void run() {
					ArrayList elements = new ArrayList();
					try {

						IType type = JavaCore.create(project).findType(
								className);

						IMethod[] methods = type.getMethods();
						for (int i = 0; i < methods.length; i++) {
							elements.add(methods[i]);
						}
					} catch (JavaModelException e) {
					}

					dialog.setElements(elements.toArray());
					dialog.setMultipleSelection(true);
					dialog.setMessage(FreemarkerUIPluginMessages.MethodSelectionDialog_label);
					 dialog.setTitle(FreemarkerUIPluginMessages.MethodSelectionDialog_title);
					dialog.create();
					// PlatformUI.getWorkbench().getHelpSystem().setHelp(dialog.getShell(),
					// IHelpContextIds.EXPORT_PACKAGES);
					SWTUtils.setDialogSize(dialog, 400, 500);
				}
			};
			BusyIndicator.showWhile(Display.getCurrent(), runnable);
			if (dialog.open() == Window.OK) {
				Object[] selected = dialog.getResult();
				if (selected != null && selected.length > 0) {
					return ((IMethod)selected[0]);
				}

			}
			labelProvider.dispose();

		} catch (CoreException e) {
		}
		return null;
	}
}
