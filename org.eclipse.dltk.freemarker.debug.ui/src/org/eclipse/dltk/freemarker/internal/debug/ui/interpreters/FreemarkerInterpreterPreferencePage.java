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
package org.eclipse.dltk.freemarker.internal.debug.ui.interpreters;

import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.ScriptInterpreterPreferencePage;

/**
 * 
 * Freemarker DLTK Interpreter Preferences Page that you can access at
 * Window/Preferences->Freemarker->Interpreters.
 * 
 */
public class FreemarkerInterpreterPreferencePage extends
		ScriptInterpreterPreferencePage {

	public static final String PAGE_ID = "org.eclipse.dltk.debug.ui.FreemarkerInterpreters";

	public InterpretersBlock createInterpretersBlock() {
		return new FreemarkerInterpretersBlock();
	}

}
