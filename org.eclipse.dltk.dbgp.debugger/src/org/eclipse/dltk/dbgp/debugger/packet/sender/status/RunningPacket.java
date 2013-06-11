package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;

public class RunningPacket extends StatusPacket {

	public RunningPacket(DbgpRequest command) {
		super(command, DbgpStatus.RUNNING, DbgpReason.OK);
	}

}
