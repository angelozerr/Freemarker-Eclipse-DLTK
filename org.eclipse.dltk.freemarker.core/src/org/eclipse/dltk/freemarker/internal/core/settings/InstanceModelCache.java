package org.eclipse.dltk.freemarker.internal.core.settings;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.freemarker.core.manager.FreemarkerTemplateManager;

import freemarker.core.Configurable;
import freemarker.provisionnal.ext.ide.SimpleEntries;
import freemarker.provisionnal.ext.ide.TemplateEntriesModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModelException;

/**
 * Cache to store the Instance model and the {@link TemplateHashModelEx} linked.
 * 
 */
public class InstanceModelCache {

	private final Object instance;
	private Map<ObjectWrapper, TemplateEntriesModel> templateHashModelMap = null;

	public InstanceModelCache(Object instance) {
		this.instance = instance;
	}

	public Object getModel() {
		return instance;
	}

	public TemplateEntriesModel getTemplateEntriesModel(Configurable configurable)
			throws TemplateModelException {
		if (configurable == null) {
			return null;
		}
		return getTemplateEntriesModel(configurable.getObjectWrapper());
	}

	public TemplateEntriesModel getTemplateEntriesModel(ObjectWrapper wrapper)
			throws TemplateModelException {
		if (instance == null) {
			return null;
		}

		if (wrapper == null) {
			return null;
		}
		if (templateHashModelMap == null) {
			templateHashModelMap = new HashMap<ObjectWrapper, TemplateEntriesModel>();
		}
		TemplateEntriesModel templateEntriesModel = templateHashModelMap
				.get(wrapper);
		if (templateEntriesModel != null) {
			return templateEntriesModel;
		}

		TemplateHashModelEx templateHashModel = FreemarkerTemplateManager.getManager()
				.getTemplateHashModelEx(instance, null, wrapper);
		if (templateHashModel != null) {
			templateEntriesModel = new SimpleEntries(templateHashModel);
			templateHashModelMap.put(wrapper, templateEntriesModel);
		}
		return templateEntriesModel;
	}

}
