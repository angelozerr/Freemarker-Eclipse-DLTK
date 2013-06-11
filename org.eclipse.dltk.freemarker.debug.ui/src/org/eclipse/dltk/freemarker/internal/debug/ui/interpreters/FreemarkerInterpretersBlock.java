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

import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;

/**
 * 
 * Call the Freemarker Interpreter dialog.
 * 
 */
public class FreemarkerInterpretersBlock extends InterpretersBlock {

	protected AddScriptInterpreterDialog createInterpreterDialog(
			IInterpreterInstall standin) {
		AddFreemarkerInterpreterDialog dialog = new AddFreemarkerInterpreterDialog(
				this, getShell(), ScriptRuntime
						.getInterpreterInstallTypes(getCurrentNature()),
				standin);
		return dialog;
	}

	protected String getCurrentNature() {
		return FreemarkerNature.NATURE_ID;
	}
}
