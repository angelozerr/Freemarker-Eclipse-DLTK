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
import org.eclipse.dltk.freemarker.core.settings.IClassLoaderProject;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;

/**
 * base class for {@link IObjectInstanceProvider}.
 */
public class BaseObjectInstanceProvider implements IObjectInstanceProvider {

	protected final IResource resource;

	private String className;
	private String methodName;

	private ProviderType type = ProviderType.UNKNOWED;

	private IClassLoaderProject project;

	private final SettingsScope scope;

	public BaseObjectInstanceProvider(IResource resource,
			IFreemarkerProjectSettings project, ProviderType type, SettingsScope scope) {
		this.resource = resource;
		this.type = type;
		this.scope = scope;
		this.project = project;
	}

	public Object getInstance() throws InstanceProviderException {
		return project.getInstance(className, methodName, type);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public ProviderType getType() {
		return type;
	}

	public SettingsScope getScope() {
		return scope;
	}
	
	public void reset() {
		this.className = null;
		this.methodName = null;		
	}
}