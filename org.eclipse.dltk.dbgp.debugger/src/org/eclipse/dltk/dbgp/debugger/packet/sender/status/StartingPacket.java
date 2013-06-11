package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;


public class StartingPacket extends StatusPacket {

	public StartingPacket(DbgpRequest command) {
		super(command, DbgpStatus.STARTING, DbgpReason.OK);
	}

}
