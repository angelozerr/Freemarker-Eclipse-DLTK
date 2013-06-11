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
package org.eclipse.dltk.freemarker.internal.ui;

import org.eclipse.dltk.freemarker.internal.ui.view.FreemarkerPreviewView;
import org.eclipse.dltk.freemarker.internal.ui.wizards.FreemarkerNewFileWizard;
import org.eclipse.dltk.freemarker.internal.ui.wizards.FreemarkerNewProjectWizard;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

/**
 * Freemarker Perspective.
 *
 */
public class FreemarkerPerspective implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "org.eclipse.dltk.freemarker.ui.FreemarkerPerspective"; //$NON-NLS-1$

	public void createInitialLayout(IPageLayout layout) {
		addFolders(layout);
		addActionSets(layout);
		addViews(layout);
		addShowViewShortcuts(layout);
		addNewWizardShortcuts(layout);
		addPerspectiveShotcuts(layout);
	}

	protected void addFolders(IPageLayout layout) {
		IFolderLayout leftFolder = layout.createFolder(
				"left", IPageLayout.LEFT, (float) 0.2, layout.getEditorArea()); //$NON-NLS-1$
		leftFolder.addView(DLTKUIPlugin.ID_SCRIPT_EXPLORER);
		leftFolder.addView("org.eclipse.dltk.testing.ResultView"); //$NON-NLS-1$
		leftFolder.addPlaceholder("org.eclipse.dltk.ui.TypeHierarchy"); //$NON-NLS-1$
		leftFolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);

		IFolderLayout bottomFolder = layout
				.createFolder(
						"bottom", IPageLayout.BOTTOM, (float) 0.75, layout.getEditorArea()); //$NON-NLS-1$
		bottomFolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottomFolder.addView(IPageLayout.ID_TASK_LIST);
		bottomFolder
				.addView(FreemarkerPreviewView.VIEW_ID); //$NON-NLS-1$
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);

		bottomFolder.addPlaceholder("org.eclipse.dltk.callhierarchy.view"); //$NON-NLS-1$
		bottomFolder.addPlaceholder(NewSearchUI.SEARCH_VIEW_ID);
		bottomFolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
	}

	protected void addActionSets(IPageLayout layout) {
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
		layout.addActionSet("org.eclipse.dltk.freemarker.ui.FreemarkerActionSet"); //$NON-NLS-1$
		layout.addActionSet("org.eclipse.debug.ui.launchActionSet"); //$NON-NLS-1$
	}

	protected void addViews(IPageLayout layout) {
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, (float) 0.75,
				layout.getEditorArea());
	}

	protected void addShowViewShortcuts(IPageLayout layout) {
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(DLTKUIPlugin.ID_SCRIPT_EXPLORER);
		layout.addShowViewShortcut("org.eclipse.dltk.testing.ResultView"); //$NON-NLS-1$
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		layout.addShowViewShortcut(FreemarkerPreviewView.VIEW_ID); //$NON-NLS-1$
		layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);
		layout.addShowViewShortcut("org.eclipse.dltk.ui.TypeHierarchy"); //$NON-NLS-1$
		layout.addShowViewShortcut("org.eclipse.dltk.callhierarchy.view"); //$NON-NLS-1$
	}

	protected void addNewWizardShortcuts(IPageLayout layout) {
		// Freemarker
		layout.addNewWizardShortcut(FreemarkerNewProjectWizard.WIZARD_ID);
		layout.addNewWizardShortcut(FreemarkerNewFileWizard.WIZARD_ID);

		// General
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$
	}

	protected void addPerspectiveShotcuts(IPageLayout layout) {
		// layout
		//		.addPerspectiveShortcut("org.eclipse.dltk.freemarker.ui.FreemarkerBrowsingPerspective"); //$NON-NLS-1$
		layout.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective"); //$NON-NLS-1$
		layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //$NON-NLS-1$
		layout.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); //$NON-NLS-1$
	}
}
