package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.packet.sender.DbgpXmlPacket;

public class ContextNamesPacket extends DbgpXmlResponsePacket {

	private static final String ID_ATTR = "id";
	private static final String NAME_ATTR = "name";
	private static final String CONTEXT_ELT = "context";

	public ContextNamesPacket(DbgpRequest command) {
		super(command);
	}
	
	public void addContext(int id, String name) {
		DbgpXmlPacket context = super.addElement(CONTEXT_ELT);
		context.addAttribute(NAME_ATTR, name);
		context.addAttribute(ID_ATTR, id);
	}

}
