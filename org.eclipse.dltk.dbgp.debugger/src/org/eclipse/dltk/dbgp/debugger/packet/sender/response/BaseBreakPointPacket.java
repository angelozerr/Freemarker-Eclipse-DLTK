package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

public class BaseBreakPointPacket extends DbgpXmlResponsePacket {

	protected static final String STATE_ATTR = "state";
	
	public BaseBreakPointPacket(DbgpRequest command) {
		super(command);
	}
	
	public int getLineno() {
		return Integer.parseInt(command.getOption(N_PARAM));
	}
	
	public String getFilename() {
		return command.getOption(F_PARAM);
	}

}
