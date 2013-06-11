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

import org.eclipse.dltk.freemarker.internal.ui.parts.StructuredViewerPart;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class StructuredViewerSection<T extends FreemarkerFormPage> extends FreemarkerSection<T> {

	protected StructuredViewerPart fViewerPart;

	private boolean fDoSelection;

	/**
	 * Constructor for StructuredViewerSection.
	 * 
	 * @param formPage
	 */
	public StructuredViewerSection(T formPage,
			Composite parent, int style, String[] buttonLabels) {
		this(formPage, parent, style, true, buttonLabels);
	}

	/**
	 * Constructor for StructuredViewerSection.
	 * 
	 * @param formPage
	 */
	public StructuredViewerSection(T formPage,
			Composite parent, int style, boolean titleBar, String[] buttonLabels) {
		super(formPage, parent, style, titleBar);
		fViewerPart = createViewerPart(buttonLabels);
		fViewerPart.setMinimumSize(50, 50);
		FormToolkit toolkit = formPage.getManagedForm().getToolkit();
		createClient(getSection(), toolkit);
		fDoSelection = true;
	}

	protected void createViewerPartControl(Composite parent, int style,
			int span, FormToolkit toolkit) {
		fViewerPart.createControl(parent, style, span, toolkit);
		MenuManager popupMenuManager = new MenuManager();
		IMenuListener listener = new IMenuListener() {
			public void menuAboutToShow(IMenuManager mng) {
				fillContextMenu(mng);
			}
		};
		popupMenuManager.addMenuListener(listener);
		popupMenuManager.setRemoveAllWhenShown(true);
		Control control = fViewerPart.getControl();
		Menu menu = popupMenuManager.createContextMenu(control);
		control.setMenu(menu);
	}

	protected Composite createClientContainer(Composite parent, int span,
			FormToolkit toolkit) {
		Composite container = toolkit.createComposite(parent);
		container.setLayout(FormLayoutFactory.createSectionClientGridLayout(
				false, span));
		return container;
	}

	protected abstract StructuredViewerPart createViewerPart(
			String[] buttonLabels);

	protected void fillContextMenu(IMenuManager manager) {
	}

	protected void buttonSelected(int index) {
	}

	protected ISelection getViewerSelection() {
		return fViewerPart.getViewer().getSelection();
	}

	/**
	 * @param targetObject
	 * @param sourceObjects
	 */
	protected void doPaste(Object targetObject, Object[] sourceObjects) {
		// NO-OP
		// Children will override to provide fuctionality
	}

	/**
	 * @param targetObject
	 * @param sourceObjects
	 * @return
	 */
	protected boolean canPaste(Object targetObject, Object[] sourceObjects) {
		return false;
	}

	public void setFocus() {
		fViewerPart.getControl().setFocus();
	}

	public StructuredViewerPart getStructuredViewerPart() {
		return this.fViewerPart;
	}

	/**
	 * <p>
	 * Given the index of TreeViewer item and the size of the array of its
	 * immediate siblings, gets the index of the desired new selection as
	 * follows:
	 * <ul>
	 * <li>if this is the only item, return -1 (meaning select the parent)</li>
	 * <li>if this is the last item, return the index of the predecessor</li>
	 * <li>otherwise, return the index of the successor</li>
	 * </p>
	 * 
	 * @param thisIndex
	 *            the item's index
	 * @param length
	 *            the array length
	 * @return new selection index or -1 for parent
	 */
	protected int getNewSelectionIndex(int thisIndex, int length) {
		if (thisIndex == length - 1)
			return thisIndex - 1;
		return thisIndex + 1;
	}

	protected int getArrayIndex(Object[] array, Object object) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(object))
				return i;
		}
		return -1;
	}

	/**
	 * @param select
	 */
	protected void doSelect(boolean select) {
		fDoSelection = select;
	}

	/**
	 * @return
	 */
	protected boolean canSelect() {
		return fDoSelection;
	}

}
