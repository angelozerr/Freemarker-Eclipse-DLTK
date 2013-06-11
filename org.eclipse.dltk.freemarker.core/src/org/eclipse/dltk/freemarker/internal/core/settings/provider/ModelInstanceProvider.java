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
package org.eclipse.dltk.freemarker.internal.core.settings.provider;

import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;
import org.eclipse.dltk.freemarker.internal.core.settings.InstanceModelCache;

import freemarker.core.Configurable;
import freemarker.provisionnal.ext.ide.TemplateEntriesModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * Model {@link Object} instance provider implementation with
 * {@link SettingsScope#TEMPLATE} scope.
 * 
 */
public class ModelInstanceProvider extends BaseObjectInstanceProvider implements
		IModelInstanceProvider {

	public ModelInstanceProvider(IResource resource,
			IFreemarkerProjectSettings project) {
		this(resource, project, SettingsScope.TEMPLATE);
	}

	public ModelInstanceProvider(IResource resource,
			IFreemarkerProjectSettings project, SettingsScope scope) {
		super(resource, project, ProviderType.MODEL, scope);
	}

	public Object getModel() {
		InstanceModelCache instanceModelCache = (InstanceModelCache) getInstance();
		if (instanceModelCache != null) {
			return instanceModelCache.getModel();
		}
		return null;
	}

	public TemplateEntriesModel getTemplateEntriesModel(Configurable configurable) throws TemplateModelException {
		InstanceModelCache instanceModelCache = (InstanceModelCache) getInstance();
		if (instanceModelCache != null) {
			return instanceModelCache.getTemplateEntriesModel(configurable);
		}
		return null;
	}

}
