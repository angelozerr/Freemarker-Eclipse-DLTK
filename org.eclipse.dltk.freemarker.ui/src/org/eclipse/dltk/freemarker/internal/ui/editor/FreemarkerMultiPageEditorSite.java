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
package org.eclipse.dltk.freemarker.internal.ui.editor;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;

/**
 * 
 * Freemarker {@link MultiPageEditorSite} to define an ID. The ID is used into
 * org.eclipse.ui.editorActions extensions :
 * 
 * <pre>
 * <extension point="org.eclipse.ui.editorActions">
 * 		<editorContribution
 * 			targetID="org.eclipse.dltk.freemarker.ui.editor.FreemarkerEditorSite"
 * 			id="org.eclipse.dltk.freemarker.ui.editor.FreemarkerEditor.BreakpointRulerActions">
 * </pre>
 * 
 */
public class FreemarkerMultiPageEditorSite extends MultiPageEditorSite {

	private static final String EDITOR_SITE_ID = "org.eclipse.dltk.freemarker.ui.editor.FreemarkerEditorSite";

	public FreemarkerMultiPageEditorSite(MultiPageEditorPart multiPageEditor,
			IEditorPart editor) {
		super(multiPageEditor, editor);
	}

	@Override
	public String getId() {
		return EDITOR_SITE_ID;
	}

}
