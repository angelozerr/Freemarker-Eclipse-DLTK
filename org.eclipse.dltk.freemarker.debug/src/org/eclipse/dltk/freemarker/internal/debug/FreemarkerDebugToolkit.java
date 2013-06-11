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
package org.eclipse.dltk.freemarker.internal.debug;

import org.eclipse.dltk.debug.core.AbstractDLTKDebugToolkit;
import org.eclipse.dltk.debug.core.IDLTKDebugToolkit;

/**
 * Freemarker DLTK {@link IDLTKDebugToolkit} implementation.
 * 
 */
public class FreemarkerDebugToolkit extends AbstractDLTKDebugToolkit {

	public boolean isAccessWatchpointSupported() {
		return true;
	}

}
