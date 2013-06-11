package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

/**
 * DBGp XML Stderr Packet. Example :
 * 
 * <pre>
 * <response command="stderr"
 *           success="0|1"
 *           transaction_id="transaction_id"/>
 * </pre>
 * 
 * @see specification at
 *      http://xdebug.org/docs-dbgp.php#stdout-stderr
 */
public class StderrPacket extends DbgpXmlResponsePacket {

	public StderrPacket(DbgpRequest command) {
		super(command);
		// FIXME : manage success by using debugger engine.
		addAttribute(SUCCESS_ATTR, ONE);
	}

}
