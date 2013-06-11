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
package org.eclipse.dltk.freemarker.ui.actions;

import org.eclipse.dltk.internal.ui.editor.DLTKEditorMessages;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.actions.DLTKActionConstants;
import org.eclipse.dltk.ui.actions.GenerateActionGroup;
import org.eclipse.dltk.ui.actions.IScriptEditorActionDefinitionIds;
import org.eclipse.jface.action.Action;

/**
 * Action group to manage comments FTL.
 * 
 */
public class FreemarkerGenerateActionGroup extends GenerateActionGroup {

	public FreemarkerGenerateActionGroup(ScriptEditor editor, String groupName) {
		super(editor, groupName);

		// AddBlockCommentAction
		Action addBlockCommentAction = new AddBlockCommentAction(
				DLTKEditorMessages.getBundleForConstructedKeys(),
				"AddBlockComment.", editor); //$NON-NLS-1$
		addBlockCommentAction
				.setActionDefinitionId(IScriptEditorActionDefinitionIds.ADD_BLOCK_COMMENT);
		editor.setAction(DLTKActionConstants.ADD_BLOCK_COMMENT,
				addBlockCommentAction);
		editor.markAsStateDependentAction(
				DLTKActionConstants.ADD_BLOCK_COMMENT, true);
		editor.markAsSelectionDependentAction(
				DLTKActionConstants.ADD_BLOCK_COMMENT, true);

		// RemoveBlockCommentAction
		Action removeBlockCommentAction = new RemoveBlockCommentAction(
				DLTKEditorMessages.getBundleForConstructedKeys(),
				"RemoveBlockComment.", editor); //$NON-NLS-1$
		removeBlockCommentAction
				.setActionDefinitionId(IScriptEditorActionDefinitionIds.REMOVE_BLOCK_COMMENT);
		editor.setAction(DLTKActionConstants.REMOVE_BLOCK_COMMENT,
				removeBlockCommentAction);
		editor.markAsStateDependentAction(
				DLTKActionConstants.REMOVE_BLOCK_COMMENT, true);
		editor.markAsSelectionDependentAction(
				DLTKActionConstants.REMOVE_BLOCK_COMMENT, true);

	}

}
