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
package org.eclipse.dltk.freemarker.core;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.environment.IFileHandle;

/**
 * Freemarker DLTK {@link IDLTKLanguageToolkit} implementation.
 * 
 */
public class FreemarkerLanguageToolkit extends AbstractLanguageToolkit
		implements IFreemarkerConstants {

	private static FreemarkerLanguageToolkit INSTANCE = new FreemarkerLanguageToolkit();

	public FreemarkerLanguageToolkit() {
	}

	public boolean languageSupportZIPBuildpath() {
		return false;
	}

	public String getNatureId() {
		return FreemarkerNature.NATURE_ID;
	}

	public static IDLTKLanguageToolkit getDefault() {
		return INSTANCE;
	}

	public String getLanguageName() {
		return FREEMARKER_LANGUAGE_NAME;
	}

	public String getLanguageContentType() {
		return FREEMARKER_LANGUAGE_CONTENT_TYPE;
	}

	public boolean canValidateContent(IResource resource) {
		return false;
	}

	public boolean canValidateContent(File file) {
		return false;
	}

	public boolean canValidateContent(IFileHandle file) {
		return false;
	}

	public String getPreferenceQualifier() {
		return FreemarkerCorePlugin.PLUGIN_ID;
	}

}
