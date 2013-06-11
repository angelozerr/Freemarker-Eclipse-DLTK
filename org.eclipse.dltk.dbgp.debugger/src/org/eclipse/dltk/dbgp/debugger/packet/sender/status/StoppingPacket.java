package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;


public class StoppingPacket extends StatusPacket {

	public StoppingPacket(DbgpRequest command) {
		super(command, DbgpStatus.STOPPING, DbgpReason.OK);
	}

}
