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

import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.core.util.ClassUtils;
import org.eclipse.dltk.freemarker.internal.ui.util.SharedLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Freemarker UI Plugin Label provider.
 * 
 */
public class FreemarkerUIPluginLabelProvider extends SharedLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IProjectObjectInstanceProvider) {
			return getObjectText(((IProjectObjectInstanceProvider) element));
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IProjectObjectInstanceProvider) {
			return getObjectImage(((IProjectObjectInstanceProvider) element));
		}
		return super.getImage(element);
	}

	// ------------- Label/Image provider for IProjectObjectInstanceProvider

	/**
	 * Return the label of {@link IProjectObjectInstanceProvider}. Ex :
	 * 
	 * <pre>
	 * My Config - MyClassProvider#getModel()
	 * </pre>
	 * 
	 * @param provider
	 * @return
	 */
	private String getObjectText(IProjectObjectInstanceProvider provider) {
		String className = provider.getClassName();
		String methodName = provider.getMethodName();
		StringBuffer label = new StringBuffer();
		label.append(provider.getName());
		label.append(" - ");
		label.append(ClassUtils.formatClassMethodName(className, methodName,
				false));
		return label.toString();
	}

	/**
	 * Return the image of {@link IProjectObjectInstanceProvider} swith the
	 * {@link ProviderType}.
	 * 
	 * @param projectObjectInstanceProvider
	 * @return
	 */
	private Image getObjectImage(
			IProjectObjectInstanceProvider projectObjectInstanceProvider) {
		switch (projectObjectInstanceProvider.getType()) {
		case CONFIGURATION:
			return get(FreemarkerUIPluginImages.DESC_CONFIGURATION_FM_OBJ);
		case MODEL:
			return get(FreemarkerUIPluginImages.DESC_MODEL_FM_OBJ);
		}
		return null;
	}
}
