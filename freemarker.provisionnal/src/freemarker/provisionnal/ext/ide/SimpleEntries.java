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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

/**
 * 
 * {@link TemplateEntriesModel} implementation with Hash Model.
 * 
 */
public class SimpleEntries extends AbstractEntries {

	private TemplateHashModelEx hashModel;

	public SimpleEntries(TemplateHashModelEx hashModel) {
		this.hashModel = hashModel;
	}

	@Override
	protected Collection<TemplateEntryModel> createEntries()
			throws TemplateModelException {
		TemplateCollectionModel collectionModel = hashModel.keys();
		if (collectionModel != null) {
			List<TemplateEntryModel> models = new ArrayList<TemplateEntryModel>();
			TemplateModelIterator modelIterator = collectionModel.iterator();
			while (modelIterator.hasNext()) {
				TemplateModel key = modelIterator.next();
				TemplateModel value = getValue(hashModel.get(key.toString()));
				models.add(new SimpleEntry(key, value));
			}
			return models;
		}
		return null;
	}
}
