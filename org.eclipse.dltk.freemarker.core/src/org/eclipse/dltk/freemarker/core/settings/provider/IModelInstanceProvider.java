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
package org.eclipse.dltk.freemarker.core.settings.provider;

import freemarker.core.Configurable;
import freemarker.provisionnal.ext.ide.TemplateEntriesModel;
import freemarker.template.Template;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModelException;

/**
 * Model {@link Object} instance provider.
 * 
 */
public interface IModelInstanceProvider extends IObjectInstanceProvider {

	/**
	 * Returns the Model used to merge it with {@link Template}.
	 * 
	 * @return
	 */
	Object getModel();

	/**
	 * Return the {@link TemplateHashModelEx} of the model swith the
	 * {@link Configurable#getObjectWrapper()}.
	 * 
	 * @param configurable
	 * @return
	 * @throws TemplateModelException
	 */
	TemplateEntriesModel getTemplateEntriesModel(Configurable configurable)
			throws TemplateModelException;
}
