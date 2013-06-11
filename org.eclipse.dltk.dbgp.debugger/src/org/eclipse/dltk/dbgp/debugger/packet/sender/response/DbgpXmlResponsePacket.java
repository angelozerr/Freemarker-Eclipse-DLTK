package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.packet.sender.DbgpXmlPacket;

public class DbgpXmlResponsePacket extends DbgpXmlPacket {

	private static final String RESPONSE_ELT = "response";
	private static final String COMMAND_ATTR = "command";
    private static final String TRANSACTION_ID_ATTR = "transaction_id";
	
    
	protected static final String ZERO = "0";
	protected static final String ONE = "1";
	
	protected static final String SUCCESS_ATTR = "success";
	
    protected final DbgpRequest command;
    
    protected static final String D_PARAM = "-d";
    protected static final String F_PARAM = "-f";
    protected static final String I_PARAM = "-i";
    protected static final String N_PARAM = "-n";
    protected static final String S_PARAM = "-s";
        
	public DbgpXmlResponsePacket(DbgpRequest command) {
        super(RESPONSE_ELT);        
        addAttribute(COMMAND_ATTR, command.getCommand());
        addAttribute(TRANSACTION_ID_ATTR, command.getOption(I_PARAM));
        this.command = command;
    }

}
