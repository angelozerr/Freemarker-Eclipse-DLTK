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
package org.eclipse.dltk.freemarker.internal.debug.ui;

import org.eclipse.dltk.debug.ui.DLTKDebugUIPluginPreferenceInitializer;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;

/**
 * Freemarker DLTK Debug UI preference initializer.
 * 
 */
public class FreemarkerDebugUIPreferenceInitializer extends
		DLTKDebugUIPluginPreferenceInitializer {

	protected String getNatureId() {
		return FreemarkerNature.NATURE_ID;
	}

}
