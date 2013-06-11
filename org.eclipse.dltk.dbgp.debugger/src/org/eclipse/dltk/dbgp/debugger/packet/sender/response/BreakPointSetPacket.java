package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

/**
 * <pre>
 * <response command="breakpoint_set"
 *           transaction_id="TRANSACTION_ID"
 *           state="STATE"
 *           id="BREAKPOINT_ID"/>
 * </pre>
 * 
 */
public class BreakPointSetPacket extends BaseBreakPointPacket {

	private static final String ID_ATTR = "id";

	public BreakPointSetPacket(DbgpRequest command) {
		super(command);
		addAttribute(STATE_ATTR, getState());
	}
	
	public String getState() {
		return command.getOption(S_PARAM);
	}
	
	public void setBreakPointId(String id) {
		super.addAttribute(ID_ATTR, id);
	}
	
}
