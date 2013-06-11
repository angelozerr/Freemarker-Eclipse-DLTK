/*******************************************************************************
 *  Copyright (c) 2003, 2009 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.freemarker.internal.ui.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.internal.ui.parts.IContextPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Base class for Section.
 */
public abstract class FreemarkerSection<T extends FreemarkerFormPage> extends
		SectionPart implements IContextPart {

	private T fPage;
	
	private Boolean internalStale = null;

	public FreemarkerSection(T page, Composite parent, int style) {
		this(page, parent, style, true);
	}

	public FreemarkerSection(T page, Composite parent, int style,
			boolean titleBar) {
		super(parent, page.getManagedForm().getToolkit(),
				titleBar ? (ExpandableComposite.TITLE_BAR | style) : style);
		fPage = page;
		initialize(page.getManagedForm());
		getSection().clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		getSection().setData("part", this); //$NON-NLS-1$
	}

	/**
	 * Create the client body.
	 * 
	 * @param section
	 * @param toolkit
	 */
	protected abstract void createClient(Section section, FormToolkit toolkit);

	/**
	 * Returns owner Freemarker page.
	 * 
	 * @return
	 */
	public T getPage() {
		return fPage;
	}

	/**
	 * Returns owner {@link IProject}.
	 * 
	 * @return
	 */
	protected IProject getProject() {
		return fPage.getFreemarkerEditor().getProject();
	}

	public IFreemarkerTemplateSettings getTemplateSettings() {
		return (IFreemarkerTemplateSettings) fPage.getFreemarkerEditor()
				.getAdapter(IFreemarkerTemplateSettings.class);
	}

	public IFreemarkerProjectSettings getProjectSettings() {
		return (IFreemarkerProjectSettings) fPage.getFreemarkerEditor()
				.getAdapter(IFreemarkerProjectSettings.class);
	}	
	public Object getAdapter(Class adapter) {
		return null;
	}

	public void fireSaveNeeded() {
		markDirty();
	}

	public boolean isEditable() {
		return true;
	}

	public void cancelEdit() {
		super.refresh();
	}

	public String getContextId() {
		return null;
	}

	@Override
	public void commit(boolean onSave) {		
		super.commit(onSave);
		if (onSave) {
			internalStale = true;
		}
	}
	
	@Override
	public void refresh() {		
		super.refresh();
		internalStale = null;
	}
	
	@Override
	public boolean isStale() {
		if (internalStale != null)
			return internalStale;
		return super.isStale();
	}
	// protected IProject getProject() {
	// return getPage().getFreemarkerEditor().getCommonProject();
	// }
}
