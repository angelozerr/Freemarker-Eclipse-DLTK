package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.debugger.event.ISuspendedEvent;

public interface IDbgpStatusFactory {

	StatusPacket processStatusStarting(
			IDbgpDebuggerEngine engine, DbgpRequest command,
			ISuspendedEvent event);
	
	StatusPacket processStatusBreak(
			IDbgpDebuggerEngine engine, DbgpRequest command,
			ISuspendedEvent event);

	StatusPacket processStatusStopping(
			IDbgpDebuggerEngine engine,
			DbgpRequest command);

	StatusPacket processStatusRunning(
			IDbgpDebuggerEngine engine,
			DbgpRequest command);

	StatusPacket processStatusStopped(
			IDbgpDebuggerEngine engine,
			DbgpRequest command);

}
