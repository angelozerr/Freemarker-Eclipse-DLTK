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

import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;

/**
 * Template entry model which represent the key/value of the keys/values
 * {@link TemplateHashModelEx}.
 * 
 */
public interface TemplateEntryModel extends TemplateModel {

	public static final TemplateEntryModel[] EMPTY_ENTRY_MODEL = new TemplateEntryModel[0];

	/**
	 * Return the entry key.
	 * 
	 * @return
	 */
	TemplateModel getKey();

	/**
	 * Return the entry value.
	 * 
	 * @return
	 */
	TemplateModel getValue();

	/**
	 * Return true if it's simple value (scalar, number, boolean, date...) and
	 * false otherwise (collection...).
	 * 
	 * @return
	 */
	boolean isSimpleValue();

	/**
	 * Return the value as an array.
	 * 
	 * @return
	 */
	TemplateModel[] getValues();
}
