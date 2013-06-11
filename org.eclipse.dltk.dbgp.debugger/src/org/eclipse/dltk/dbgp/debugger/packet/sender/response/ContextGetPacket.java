package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.IVariableAdder;

public class ContextGetPacket extends DbgpXmlResponsePacket implements IVariableAdder  {

	private static final String CONTEXT_ATTR = "context";

	public ContextGetPacket(DbgpRequest command) {
		super(command);
		String contextID = command.getOption("-c");
        super.addAttribute(CONTEXT_ATTR, contextID);
	}

	public int getContextId() {
		return Integer.parseInt(getAttribute(CONTEXT_ATTR));
	}
	
	public void addProperty(PropertyPacket property) {
		super.addElement(property);
	}

	public void addVariable(String name, String type, String value) {
		addProperty(new PropertyPacket(name, type, value));		
	}
}
