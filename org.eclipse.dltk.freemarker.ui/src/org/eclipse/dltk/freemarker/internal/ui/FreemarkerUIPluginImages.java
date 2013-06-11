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

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.ui.PluginImagesHelper;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Freemarker UI plugin images.
 * 
 */
public class FreemarkerUIPluginImages {

	private static final String NAME_PREFIX = FreemarkerUIPlugin.PLUGIN_ID
			+ "."; //$NON-NLS-1$

	private static ImageRegistry PLUGIN_REGISTRY;

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$

	/**
	 * Set of predefined Image Descriptors.
	 */
	private static final String PATH_OBJ = ICONS_PATH + "obj16/"; //$NON-NLS-1$
	private static final String PATH_LCL = ICONS_PATH + "elcl16/"; //$NON-NLS-1$
	private static final String PATH_TOOL = ICONS_PATH + "etool16/"; //$NON-NLS-1$
	private static final String PATH_WIZBAN = ICONS_PATH
			+ PluginImagesHelper.T_WIZBAN + "/"; //$NON-NLS-1$

	// Freemarker
	public static final ImageDescriptor DESC_OVERVIEW_FM_OBJ = create(PATH_OBJ,
			"overview.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CONFIGURATION_FM_OBJ = create(
			PATH_OBJ, "configuration.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_MODEL_FM_OBJ = create(PATH_OBJ,
			"model.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_PREVIEW_FM_OBJ = create(PATH_OBJ,
			"preview.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SET_AS_PROJECT_LEVEL_FM_OBJ = create(
			PATH_TOOL, "setprjlevel.gif"); //$NON-NLS-1$
	public static final String OBJ_DESC_SET_AS_PROJECT_LEVEL_FM_OBJ = NAME_PREFIX
			+ "DESC_SET_AS_PROJECT_LEVEL_FM_OBJ"; //$NON-NLS-1$
	public static final ImageDescriptor DESC_MODEL_PROVIDER_FM_OBJ = create(
			PATH_TOOL, "modelprovider.gif"); //$NON-NLS-1$
	public static final String OBJ_DESC_MODEL_PROVIDER_FM_OBJ = NAME_PREFIX
			+ "DESC_MODEL_PROVIDER_FM_OBJ"; //$NON-NLS-1$
	public static final ImageDescriptor DESC_CONFIGURATION_PROVIDER_FM_OBJ = create(
			PATH_TOOL, "cfgprovider.gif"); //$NON-NLS-1$
	public static final String OBJ_DESC_CONFIGURATION_PROVIDER_FM_OBJ = NAME_PREFIX
			+ "DESC_CONFIGURATION_PROVIDER_FM_OBJ"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_HELP = create(PATH_LCL, "help.gif"); //$NON-NLS-1$

	// Java
	public static final ImageDescriptor DESC_GENERATE_CLASS = create(PATH_LCL,
			"generate_class.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_GENERATE_INTERFACE = create(
			PATH_LCL, "generate_interface.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_PACKAGE_OBJ = create(PATH_LCL,
			"package_obj.gif"); //$NON-NLS-1$

	public static final String OBJ_DESC_GENERATE_CLASS = NAME_PREFIX
			+ "OBJ_DESC_GENERATE_CLASS"; //$NON-NLS-1$
	public static final String OBJ_DESC_GENERATE_INTERFACE = NAME_PREFIX
			+ "OBJ_DESC_GENERATE_INTERFACE"; //$NON-NLS-1$
	public static final String OBJ_DESC_PACKAGE = NAME_PREFIX
			+ "OBJ_DESC_PACKAGE"; //$NON-NLS-1$

	// Run/Debug
	public static final ImageDescriptor DESC_RUN_EXC = create(PATH_OBJ,
			"run_exc.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_DEBUG_EXC = create(PATH_OBJ,
			"debug_exc.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_PAGE_OBJ = create(PATH_OBJ,
			"page_obj.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_WIZBAN_PROJECT_CREATION = create(
			PATH_WIZBAN, "projectcreate_wiz.png"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_WIZBAN_FILE_CREATION = create(
			PATH_WIZBAN, "filecreate_wiz.png"); //$NON-NLS-1$

	private static ImageDescriptor create(String prefix, String name) {
		return ImageDescriptor.createFromURL(makeImageURL(prefix, name));
	}

	private static URL makeImageURL(String prefix, String name) {
		String path = "$nl$/" + prefix + name; //$NON-NLS-1$
		return FileLocator.find(FreemarkerUIPlugin.getDefault().getBundle(),
				new Path(path), null);
	}

	public static Image get(String key) {
		if (PLUGIN_REGISTRY == null)
			initialize();
		return PLUGIN_REGISTRY.get(key);
	}

	/* package */
	private static final void initialize() {
		PLUGIN_REGISTRY = new ImageRegistry();
		manage(OBJ_DESC_GENERATE_CLASS, DESC_GENERATE_CLASS);
		manage(OBJ_DESC_GENERATE_INTERFACE, DESC_GENERATE_INTERFACE);
		manage(OBJ_DESC_PACKAGE, DESC_PACKAGE_OBJ);
		manage(OBJ_DESC_SET_AS_PROJECT_LEVEL_FM_OBJ,
				DESC_SET_AS_PROJECT_LEVEL_FM_OBJ);
		manage(OBJ_DESC_CONFIGURATION_PROVIDER_FM_OBJ,
				DESC_CONFIGURATION_PROVIDER_FM_OBJ);
		manage(OBJ_DESC_MODEL_PROVIDER_FM_OBJ, DESC_MODEL_PROVIDER_FM_OBJ);

	}

	public static Image manage(String key, ImageDescriptor desc) {
		Image image = desc.createImage();
		PLUGIN_REGISTRY.put(key, image);
		return image;
	}

}
