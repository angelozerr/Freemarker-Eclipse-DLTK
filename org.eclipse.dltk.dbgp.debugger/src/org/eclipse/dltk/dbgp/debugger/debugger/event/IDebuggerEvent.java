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
package org.eclipse.dltk.dbgp.debugger.debugger.event;

/**
 * Debugger events.
 * 
 */
public interface IDebuggerEvent {

	public enum Type {
		STARTING, RUNNING, SUSPENDED, STOPPING, STOPPED, STDOUT_STREAM, STDERR_STREAM
	}

	/**
	 * Return the type of the debugger event.
	 * 
	 * @return
	 */
	Type getType();
}
