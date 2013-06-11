package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;

public class BaseFeaturePacket extends DbgpXmlResponsePacket {

	private static final String N_PARAM = "-n";

	public BaseFeaturePacket(DbgpRequest command) {
		super(command);
	}
	
	protected String getFeatureName() {
		return command.getOption(N_PARAM);
	}

}
