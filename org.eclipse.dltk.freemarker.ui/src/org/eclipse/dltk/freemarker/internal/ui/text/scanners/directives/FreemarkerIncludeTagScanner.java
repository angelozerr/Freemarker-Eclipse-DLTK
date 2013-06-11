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
package org.eclipse.dltk.freemarker.internal.ui.text.scanners.directives;

import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 *  Scanner for FTL 'include' <#include... >.
 *
 */
public class FreemarkerIncludeTagScanner extends
		AbstractFreemarkerDirectiveScanner {

	public FreemarkerIncludeTagScanner(TagSyntaxProvider tagSyntaxProvider,
			IColorManager manager, IPreferenceStore store) {
		super(INCLUDE_DIRECTIVE, tagSyntaxProvider, manager, store);
		initialize();
	}
}
