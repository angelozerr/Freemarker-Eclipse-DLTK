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
package org.eclipse.dltk.dbgp.debugger.debugger;

import org.eclipse.dltk.dbgp.debugger.IVariableAdder;
import org.eclipse.dltk.dbgp.debugger.debugger.event.IDebuggerEventListener;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.ContextGetPacket;

/**
 * Debugger API.
 * 
 */
public interface IDebugger {

	/**
	 * Run the debugger
	 */
	void run();

	/**
	 * Resume the debugger.
	 */
	void resume();

	/**
	 * Suspend the debugger.
	 */
	void suspend();

	/**
	 * Stop the debugger.
	 */
	void stop();

	/**
	 * Add breakpoint
	 * 
	 * @param lineno
	 *            the line number in the filename to debug where to put the
	 *            breakpoint
	 * @param filename
	 *            the filename to debug
	 * @return id which indentify the breakpoint.
	 */
	String addBreakPoint(int lineno, String filename);

	/**
	 * Remove the breakpoint indetified with breakPointId.s
	 * 
	 * @param breakPointId
	 *            id of the breakpoint to remove.
	 */
	void removeBreakPoint(String breakPointId);

	/**
	 * Evaluate the expression.
	 * 
	 * @param expression
	 * @param transactionId
	 */
	void evaluate(String expression, int transactionId);

	/**
	 * Add debugger listener.
	 * 
	 * @param listener
	 */
	void addDebuggerEventListener(IDebuggerEventListener listener);

	/**
	 * Remove debugger listener.
	 * 
	 * @param listener
	 */
	void removeDebuggerEventListener(IDebuggerEventListener listener);

	/**
	 * Returns the sate of the debugger.
	 * 
	 * @return
	 */
	DebuggerState getState();

	/**
	 * 
	 * @param contextId
	 * @param variableAdder
	 */
	void collectVariables(int contextId, IVariableAdder variableAdder);

}
