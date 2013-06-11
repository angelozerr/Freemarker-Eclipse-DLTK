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
package org.eclipse.dltk.freemarker.internal.launching;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;

/**
 * Freemarker DLTK {@link IInterpreterInstall} implementation.
 * 
 */
public class GenericFreemarkerInstall extends AbstractInterpreterInstall {

	//	private static final String BUILTINS_JS = "builtins.js"; //$NON-NLS-1$
	//
	// public String getBuiltinModuleContent(String name) {
	// InputStream stream = GenericFreemarkerInstall.class
	// .getResourceAsStream(BUILTINS_JS);
	// DataInputStream st = new DataInputStream(stream);
	// StringBuffer buf = new StringBuffer();
	// try {
	// while (st.available() >= 0) {
	// String line = st.readLine();
	// if (line == null)
	// break;
	// buf.append(line);
	// buf.append('\n');
	// }
	//
	// } catch (IOException e) {
	// // should not happen
	// }
	// return buf.toString();
	// }
	//
	// public String[] getBuiltinModules() {
	//		return new String[] { "builtins.js" }; //$NON-NLS-1$
	// }
	//
	// public long lastModified() {
	// try {
	// return GenericFreemarkerInstall.class.getResource(BUILTINS_JS)
	// .openConnection().getLastModified();
	// } catch (IOException e) {
	// return 0;
	// }
	// }

	public GenericFreemarkerInstall(IInterpreterInstallType type, String id) {
		super(type, id);
	}

	public IInterpreterRunner getInterpreterRunner(String mode) {
		IInterpreterRunner runner = super.getInterpreterRunner(mode);
		if (runner != null) {
			return runner;
		}

		if (mode.equals(ILaunchManager.RUN_MODE)) {
			return new FreemarkerInterpreterRunner(this);
		}
		return null;
	}

	public String getNatureId() {
		return FreemarkerNature.NATURE_ID;
	}
}
