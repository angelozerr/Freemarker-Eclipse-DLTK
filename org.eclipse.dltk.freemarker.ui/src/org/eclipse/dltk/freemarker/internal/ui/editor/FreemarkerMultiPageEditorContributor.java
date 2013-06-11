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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.freemarker.internal.ui.editor.template.TemplateEditor;
import org.eclipse.dltk.internal.ui.editor.SourceModuleEditorActionContributor;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

/**
 * 
 * Freemarker editor contributor to set the DLTK {@link TemplateEditor} as
 * active editor and use DLTK {@link SourceModuleEditorActionContributor}.
 * 
 */
public class FreemarkerMultiPageEditorContributor extends
		MultiPageEditorActionBarContributor {

	private SourceModuleEditorActionContributor sourceContributor;
	private FreemarkerMultiPageEditor fEditor;
	private IFormPage fPage;
	private SubActionBars sourceActionBars;

	public FreemarkerMultiPageEditorContributor() {
		sourceContributor = createSourceContributor();
	}

	private SourceModuleEditorActionContributor createSourceContributor() {
		return new SourceModuleEditorActionContributor();
	}

	@Override
	public void init(IActionBars bars, IWorkbenchPage page) {	
		super.init(bars, page);
		sourceActionBars = new SubActionBars(bars);
		sourceContributor.init(sourceActionBars, page);
	}

	public void setActiveEditor(IEditorPart targetEditor) {
		if (!(targetEditor instanceof FreemarkerMultiPageEditor))
			return;

		this.fEditor = (FreemarkerMultiPageEditor) targetEditor;
		setActivePage(fEditor.getActiveEditor());

	}

	public void setActivePage(IEditorPart newEditor) {
		if (fEditor == null)
			return;

		IFormPage oldPage = fPage;
		fPage = fEditor.getActivePageInstance();
		if (fPage == null)
			return;

		if (oldPage != null && !oldPage.isEditor() && !fPage.isEditor()) {
			getActionBars().updateActionBars();
			return;
		}

		boolean isSourcePage = fPage instanceof TemplateEditor;
		if (isSourcePage && fPage.equals(oldPage))
			return;
		sourceContributor.setActiveEditor(fPage);
		setSourceActionBarsActive(isSourcePage);
	}

	protected void setSourceActionBarsActive(boolean active) {
		IActionBars rootBars = getActionBars();
		rootBars.clearGlobalActionHandlers();
		rootBars.updateActionBars();
		if (active) {
			sourceActionBars.activate();
			Map handlers = sourceActionBars.getGlobalActionHandlers();
			if (handlers != null) {
				Set keys = handlers.keySet();
				for (Iterator iter = keys.iterator(); iter.hasNext();) {
					String id = (String) iter.next();
					rootBars.setGlobalActionHandler(id, (IAction) handlers
							.get(id));
				}
			}
		} else {
			sourceActionBars.deactivate();
		}

		rootBars.updateActionBars();
	}

	@Override
	public void dispose() {		
		sourceActionBars.dispose();
		sourceContributor.dispose();		
		super.dispose();
	}
}
