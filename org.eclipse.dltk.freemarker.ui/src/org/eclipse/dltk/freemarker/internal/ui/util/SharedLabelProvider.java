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
package org.eclipse.dltk.freemarker.internal.ui.util;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Shared Label provider.
 *
 */
public class SharedLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public static final int F_EDIT = 8;
	
	private Map<Object, Image> images = new Hashtable<Object, Image>();

	public Image get(ImageDescriptor desc) {
		return get(desc, 0);
	}

	public Image get(ImageDescriptor desc, int flags) {
		Object key = desc;

		if (flags != 0) {
			key = getKey(desc.hashCode(), flags);
		}
		Image image = images.get(key);
		if (image == null) {
			image = createImage(desc, flags);
			images.put(key, image);
		}
		return image;
	}

	private String getKey(long hashCode, int flags) {
		return ("" + hashCode) + ":" + flags; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private Image createImage(ImageDescriptor baseDesc, int flags) {
		return baseDesc.createImage();
	}

	public String getColumnText(Object obj, int index) {
		return getText(obj);
	}

	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}

}
