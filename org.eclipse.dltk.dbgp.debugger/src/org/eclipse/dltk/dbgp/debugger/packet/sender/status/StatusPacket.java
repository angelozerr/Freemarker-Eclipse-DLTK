package org.eclipse.dltk.dbgp.debugger.packet.sender.status;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.DbgpXmlResponsePacket;

public class StatusPacket extends DbgpXmlResponsePacket {

	private static final String REASON_ATTR = "reason";
	private static final String STATUS_ATTR = "status";

	public StatusPacket(DbgpRequest command, DbgpStatus status,
			DbgpReason reason) {
		super(command);
		addAttribute(STATUS_ATTR, status.name().toLowerCase());
		setReason(reason);
	}

	public void setReason(DbgpReason reason) {
		addAttribute(REASON_ATTR, reason.name().toLowerCase());
	}

}
