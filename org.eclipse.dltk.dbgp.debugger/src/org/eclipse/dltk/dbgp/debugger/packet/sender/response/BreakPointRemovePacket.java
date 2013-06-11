package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

/**
 * DBGp XML breakpoint_remove response.
 * 
 * <pre>
 * IDE to debugger engine:
 * 
 * breakpoint_remove -i TRANSACTION_ID -d BREAKPOINT_ID
 * 
 * debugger engine to IDE:
 * 
 * <response command="breakpoint_remove"
 *           transaction_id="TRANSACTION_ID"/>
 * </pre>
 * 
 * @see specification at http://xdebug.org/docs-dbgp.php#id4
 */
public class BreakPointRemovePacket extends DbgpXmlResponsePacket {

	public BreakPointRemovePacket(DbgpRequest command) {
		super(command);
	}

	public String getBreakPointId() {
		return command.getOption(D_PARAM);
	}

}
