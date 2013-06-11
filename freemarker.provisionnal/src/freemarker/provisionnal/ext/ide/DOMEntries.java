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

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNodeModel;
import freemarker.template.TemplateSequenceModel;

/**
 * 
 * {@link TemplateEntriesModel} implementation with DOM Model.
 */
public class DOMEntries extends AbstractEntries {

	private TemplateNodeModel nodeModel;

	public DOMEntries(TemplateNodeModel nodeModel) {
		this.nodeModel = nodeModel;
	}

	@Override
	protected Collection<TemplateEntryModel> createEntries()
			throws TemplateModelException {
		TemplateSequenceModel sequenceModel = nodeModel.getChildNodes();
		int size = sequenceModel.size();
		if (size < 1)
			return null;

		TemplateModel key = null;
		List<TemplateEntryModel> models = new ArrayList<TemplateEntryModel>();
		for (int i = 0; i < size; i++) {
			TemplateModel value = sequenceModel.get(i);
			if (value instanceof TemplateNodeModel) {
				key = new SimpleScalar(
						((TemplateNodeModel) value).getNodeName());
			}
			value = getValue(value);
			SimpleEntry entry = new SimpleEntry(key, value);
			models.add(entry);
		}

		return models;
	}

}
