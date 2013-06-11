package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;


public class StoppedPacket extends StatusPacket {

	public StoppedPacket(DbgpRequest command) {
		super(command, DbgpStatus.STOPPED, DbgpReason.OK);
	}

}
