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

import java.util.Collection;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.DbgpContext;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.debugger.BreakPointLocation;
import org.eclipse.dltk.dbgp.debugger.packet.sender.status.RunningPacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.status.StoppedPacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.status.StoppingPacket;

/**
 * Default implementation for {@link IDbgpResponseFactory}.
 * 
 */
public class DefaultDbgpResponseFactory implements IDbgpResponseFactory {

	public static final IDbgpResponseFactory INSTANCE = new DefaultDbgpResponseFactory();

	public DbgpXmlResponsePacket processCommandFeatureSet(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		return new FeatureSetPacket(command);
	}

	public DbgpXmlResponsePacket processCommandFeatureGet(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		return new FeatureGetPacket(command, engine);
	}

	public DbgpXmlResponsePacket processCommandStdout(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		return new StdoutPacket(command);
	}

	public DbgpXmlResponsePacket processCommandStderr(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		return new StderrPacket(command);
	}

	public DbgpXmlResponsePacket processCommandStop(IDbgpDebuggerEngine engine,
			DbgpRequest command) {
		// TODO : manage stopping, stopped status
		return null;
	}

	public DbgpXmlResponsePacket processCommandRunning(
			IDbgpDebuggerEngine abstractDbgpDebuggerEngine, DbgpRequest command) {
		return new RunningPacket(command);
	}

	public DbgpXmlResponsePacket processCommandStopping(
			IDbgpDebuggerEngine abstractDbgpDebuggerEngine, DbgpRequest command) {
		return new StoppingPacket(command);
	}

	public DbgpXmlResponsePacket processCommandStopped(
			IDbgpDebuggerEngine abstractDbgpDebuggerEngine, DbgpRequest command) {
		return new StoppedPacket(command);
	}

	public BreakPointSetPacket processCommandBreakPointSet(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		return new BreakPointSetPacket(command);
	}

	public BreakPointRemovePacket processCommandBreakPointRemove(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		return new BreakPointRemovePacket(command);
	}

	public DbgpXmlResponsePacket processCommandStackGet(
			IDbgpDebuggerEngine engine, DbgpRequest command,
			BreakPointLocation location) {
		String filename = location.getFileName();

		int lineno = location.getLineBegin();
		String cmdbegin = "-1:-1";
		String cmdend = "-1:-1";

		StackGetPacket packet = new StackGetPacket(command);
		packet.setData("<stack " + "level='0' " + "type='file' " + "filename='"
				+ filename + "' " + "where='&lt;template&gt;' "
				// "filename='xquery://main_module' " +
				+ "lineno='" + lineno + "' " + "cmdbegin='" + cmdbegin + "' "
				+ "cmdend='" + cmdend + "' " + "/>");

		return packet;
	}

	public DbgpXmlResponsePacket processCommandBreak(
			IDbgpDebuggerEngine engine, DbgpRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public DbgpXmlResponsePacket processCommandBreakPointGet(
			IDbgpDebuggerEngine engine, DbgpRequest request) {
		DbgpXmlResponsePacket response = new DbgpXmlResponsePacket(request);
		response.addAttribute("success", "0");
		return response;
	}

	public ContextGetPacket processCommandContextGet(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		return new ContextGetPacket(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.dbgp.debugger.packet.sender.response.IDbgpResponseFactory
	 * #
	 * processCommandContextNames(org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine
	 * , org.eclipse.dltk.dbgp.DbgpRequest)
	 */
	public DbgpXmlResponsePacket processCommandContextNames(
			IDbgpDebuggerEngine engine, DbgpRequest command) {
		ContextNamesPacket packet = new ContextNamesPacket(command);
		Collection<DbgpContext> contexts = engine.getDbgpContexts();
		for (DbgpContext context : contexts) {
			packet.addContext(context.getId(), context.getName());
		}
		return packet;
	}

	public DbgpXmlResponsePacket processCommandStepOver(
			IDbgpDebuggerEngine engine, DbgpRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
