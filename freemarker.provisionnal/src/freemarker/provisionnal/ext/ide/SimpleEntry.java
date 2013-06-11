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
package freemarker.provisionnal.ext.ide;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * 
 * {@link TemplateEntryModel} implementation.
 * 
 */
public class SimpleEntry implements TemplateEntryModel {

	private static final TemplateModel[] EMPTY_TEMPLATE_MODEL = new TemplateModel[0];
	private TemplateModel key;
	private TemplateModel value;
	private TemplateModel[] values;

	public SimpleEntry(TemplateModel key, TemplateModel value) {
		this.key = key;
		this.value = value;
	}

	public TemplateModel getKey() {
		return key;
	}

	public TemplateModel getValue() {
		return value;
	}

	public boolean isSimpleValue() {
		if (value == null) {
			return true;
		}
		if (value instanceof TemplateScalarModel) {
			return true;
		}
		if (value instanceof TemplateNumberModel) {
			return true;
		}
		if (value instanceof TemplateBooleanModel) {
			return true;
		}
		if (value instanceof TemplateDateModel) {
			return true;
		}
		return false;
	}

	public TemplateModel[] getValues() {
		if (values != null) {
			return values;
		}
		if (value == null) {
			values = EMPTY_TEMPLATE_MODEL;
		} else {
			values = new TemplateModel[1];
			values[0] = value;
		}
		return values;
	}
}
