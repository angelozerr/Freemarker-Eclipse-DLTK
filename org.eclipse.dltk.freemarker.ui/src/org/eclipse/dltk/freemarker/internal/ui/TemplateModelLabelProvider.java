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

import org.eclipse.dltk.freemarker.internal.ui.util.SharedLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import freemarker.provisionnal.ext.ide.TemplateEntryModel;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * {@link LabelProvider} to display Freemarker {@link TemplateModel}.
 */
public class TemplateModelLabelProvider extends SharedLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof TemplateScalarModel) {
			return getObjectText(((TemplateScalarModel) element));
		}
		if (element instanceof TemplateNumberModel) {
			return getObjectText(((TemplateNumberModel) element));
		}
		if (element instanceof TemplateBooleanModel) {
			return getObjectText(((TemplateBooleanModel) element));
		}		
		if (element instanceof TemplateEntryModel) {
			return getObjectText(((TemplateEntryModel) element));
		}
		return super.getText(element);
	}

	private String getObjectText(TemplateScalarModel element) {
		try {
			return element.getAsString();
		} catch (TemplateModelException e) {
			return element.toString();
		}
	}
	
	private String getObjectText(TemplateNumberModel element) {
		try {
			return element.getAsNumber() + "";
		} catch (TemplateModelException e) {
			return element.toString();
		}
	}


	private String getObjectText(TemplateBooleanModel element) {
		try {
			return element.getAsBoolean() + "";
		} catch (TemplateModelException e) {
			return element.toString();
		}
	}
	
	private String getObjectText(TemplateEntryModel element) {
		TemplateModel key = element.getKey();
		TemplateModel value = element.getValue();
		if (value!= null && element.isSimpleValue()) {
			return getText(key) + "=" +  getText(value);
		}
		return getText(key);
	}

}
