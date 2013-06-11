package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;

public class BreakPacket extends StatusPacket {

	public BreakPacket(DbgpRequest command) {
		super(command, DbgpStatus.BREAK, DbgpReason.OK);
	}

}
