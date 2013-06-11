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
package org.eclipse.dltk.freemarker.internal.debug.ui;

import org.eclipse.dltk.debug.ui.ScriptDebugModelPresentation;
import org.eclipse.dltk.freemarker.ui.IFreemarkerUIPluginConstants;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.ui.IEditorInput;

/**
 * Freemarker Debug Model Presentation. This class is used to set the focus of
 * the Freemarker editor when Freemarker debug is launched, set line highlight
 * to the breakpoint when debug stop to this breakpoint.
 * 
 */
public class FreemarkerDebugModelPresentation extends
		ScriptDebugModelPresentation implements IFreemarkerUIPluginConstants {

	public String getEditorId(IEditorInput input, Object element) {
		String editorId = EditorUtility.getEditorID(input, element);
		if (editorId == null) {
			editorId = FREEMARKER_EDITOR_ID;
		}
		return editorId;
	}

}
