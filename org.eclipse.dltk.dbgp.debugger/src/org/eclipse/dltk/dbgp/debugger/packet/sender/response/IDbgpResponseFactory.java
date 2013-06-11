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
package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.debugger.BreakPointLocation;

/**
 * XML DBGp response factory.
 * 
 */
public interface IDbgpResponseFactory {

	DbgpXmlResponsePacket processCommandFeatureSet(IDbgpDebuggerEngine engine,
			DbgpRequest command);

	DbgpXmlResponsePacket processCommandFeatureGet(IDbgpDebuggerEngine engine,
			DbgpRequest command);

	DbgpXmlResponsePacket processCommandStdout(IDbgpDebuggerEngine engine,
			DbgpRequest command);

	DbgpXmlResponsePacket processCommandStderr(IDbgpDebuggerEngine engine,
			DbgpRequest command);

	DbgpXmlResponsePacket processCommandBreak(IDbgpDebuggerEngine engine,
			DbgpRequest command);

	DbgpXmlResponsePacket processCommandStackGet(IDbgpDebuggerEngine engine,
			DbgpRequest command, BreakPointLocation location);

	BreakPointSetPacket processCommandBreakPointSet(IDbgpDebuggerEngine engine,
			DbgpRequest command);

	DbgpXmlResponsePacket processCommandBreakPointGet(
			IDbgpDebuggerEngine engine, DbgpRequest command);

	BreakPointRemovePacket processCommandBreakPointRemove(
			IDbgpDebuggerEngine engine, DbgpRequest command);

	/**
	 * Return XML DBGp packet for the command "context_names"
	 * 
	 * @param engine
	 *            the DBGp engine
	 * @param command
	 *            the DBGp request command
	 * @see DBGP specification http://xdebug.org/docs-dbgp.php#context-names
	 * 
	 * @return
	 */
	DbgpXmlResponsePacket processCommandContextNames(
			IDbgpDebuggerEngine engine, DbgpRequest command);

	ContextGetPacket processCommandContextGet(IDbgpDebuggerEngine engine,
			DbgpRequest command);

	DbgpXmlResponsePacket processCommandStepOver(IDbgpDebuggerEngine engine,
			DbgpRequest command);

}
