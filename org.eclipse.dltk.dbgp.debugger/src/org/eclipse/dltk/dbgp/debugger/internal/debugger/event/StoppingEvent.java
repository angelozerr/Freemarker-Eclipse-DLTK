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
package org.eclipse.dltk.dbgp.debugger.internal.debugger.event;

import org.eclipse.dltk.dbgp.debugger.debugger.event.IDebuggerEvent;

/**
 * Stopping debugger event.
 * 
 */
public class StoppingEvent extends AbstractDebuggerEvent {

	public final static IDebuggerEvent INSTANCE = new StoppingEvent();
	
	protected StoppingEvent() {
		super(Type.STOPPING);
	}

}
