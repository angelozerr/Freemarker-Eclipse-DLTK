package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.debugger.event.ISuspendedEvent;
import org.eclipse.dltk.dbgp.debugger.debugger.event.ISuspendedEvent.SuspendedCause;

public class DefaultDbgpStatusFactory implements IDbgpStatusFactory {

	public static final IDbgpStatusFactory INSTANCE = new DefaultDbgpStatusFactory();

	public StatusPacket processStatusStarting(IDbgpDebuggerEngine engine,
			DbgpRequest command, ISuspendedEvent event) {
		return new StartingPacket(command);
	}

	public StatusPacket processStatusBreak(IDbgpDebuggerEngine engine,
			DbgpRequest command, ISuspendedEvent event) {
		if (event.getCause() == SuspendedCause.USER) {
			// send an answer to a break command
			// response.addAttribute("success", "1");
			return null;
		} else {
			// send an answer to a continuation command

			return new BreakPacket(command);
		}
	}

	public StatusPacket processStatusRunning(IDbgpDebuggerEngine engine,
			DbgpRequest command) {
		return new RunningPacket(command);
	}

	public StatusPacket processStatusStopped(IDbgpDebuggerEngine engine,
			DbgpRequest command) {
		return new StoppedPacket(command);
	}

	public StatusPacket processStatusStopping(IDbgpDebuggerEngine engine,
			DbgpRequest command) {
		return new StoppingPacket(command);
	}

}
