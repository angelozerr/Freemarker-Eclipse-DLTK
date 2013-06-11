package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

/**
 * 
 * 
 * http://xdebug.org/docs-dbgp.php#stack-get
 */
public class StackGetPacket extends DbgpXmlResponsePacket {

	public StackGetPacket(DbgpRequest command) {
		super(command);
	}

}
