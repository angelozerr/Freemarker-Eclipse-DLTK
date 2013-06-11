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

import java.util.Collection;

import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNodeModel;

/**
 * Abstract class for {@link TemplateEntriesModel} implementation.
 * 
 */
public abstract class AbstractEntries implements TemplateEntriesModel {

	private TemplateEntryModel[] entries = null;

	public TemplateEntryModel[] entries() throws TemplateModelException {
		if (entries != null) {
			return entries;
		}

		Collection<TemplateEntryModel> models = createEntries();
		if (models != null) {
			entries = models.toArray(TemplateEntryModel.EMPTY_ENTRY_MODEL);
		}
		if (entries == null) {
			entries = TemplateEntryModel.EMPTY_ENTRY_MODEL;
		}
		return entries;
	}

	protected abstract Collection<TemplateEntryModel> createEntries()
			throws TemplateModelException;

	protected TemplateModel getValue(TemplateModel value) {
		if (value instanceof TemplateNodeModel) {
			return new DOMEntries((TemplateNodeModel) value);
		}
		if (value instanceof TemplateHashModelEx) {
			return new SimpleEntries((TemplateHashModelEx) value);
		}
		return value;
	}

	public void findDataModel(final String[] paths,
			final TemplateModelCollector collector)
			throws TemplateModelException {
		TemplateEntryModel[] entries = this.entries();
		if (entries.length < 1) {
			return;
		}
		findDataModel(entries, paths, 0, collector);
	}

	private void findDataModel(final TemplateEntryModel[] entries,
			final String[] paths, int indexPath,
			final TemplateModelCollector collector)
			throws TemplateModelException {
		if (indexPath >= paths.length)
			return;

		String path = paths[indexPath];
		boolean lastPath = (indexPath == paths.length - 1);
		for (TemplateEntryModel entry : entries) {
			String key = entry.getKey().toString();

			if (lastPath) {
				if (key.startsWith(path)) {
					TemplateModel modelKey = entry.getKey();
					TemplateModel modelValue = entry.getValue();
					collector.collect(modelKey, modelValue, paths);
				}
			} else {
				if (key.equals(path)) {
					TemplateModel model = entry.getValue();
					if (model instanceof TemplateEntriesModel) {
						findDataModel(((TemplateEntriesModel) model).entries(),
								paths, ++indexPath, collector);
					}
				}
			}
		}
		// }
	}

}
