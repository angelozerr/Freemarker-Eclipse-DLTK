package org.eclipse.dltk.dbgp.debugger.internal.packet.receiver;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.DbgpResponseErrors;
import org.eclipse.dltk.dbgp.exceptions.DbgpProtocolException;
import org.eclipse.dltk.dbgp.internal.utils.Base64Helper;

public class DbgpRequestParser {

	private static final String COMMAND_VALIDATE_REGEXP = "(\\w+)\\s(.+)";
	private static final String COMMAND_SEPARATOR_REGEXP = "\\s";
	private static final String COMMAND_DATA_TOKEN = "--";

	public static DbgpRequest parse(String cmdStr) throws DbgpProtocolException {

		if (!cmdStr.matches(COMMAND_VALIDATE_REGEXP))
			throw new DbgpProtocolException(
					DbgpResponseErrors.DbgpError.COMMAND_PARSE_ERROR
							.getMessage());
		String[] parts = cmdStr.split(COMMAND_SEPARATOR_REGEXP, 2);

		DbgpRequest request = new DbgpRequest(parts[0]);
		String[] args = parts[1].split(COMMAND_SEPARATOR_REGEXP);
		int argc = args.length;
		if (args[args.length - 2].equals(COMMAND_DATA_TOKEN)) {
			request.setData(Base64Helper.decodeString(args[args.length - 1]));
			argc = argc - 2;
		}

		for (int i = 0; i < argc; i += 2) {
			request.addOption(args[i], args[i + 1]);
		}
		return request;
	}
}
