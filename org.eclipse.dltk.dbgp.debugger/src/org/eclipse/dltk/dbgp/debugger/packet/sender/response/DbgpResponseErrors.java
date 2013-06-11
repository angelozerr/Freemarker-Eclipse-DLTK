package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbgpResponseErrors {

	private static final Properties properties = new Properties();
	static {
		InputStream stream = DbgpResponseErrors.class
				.getResourceAsStream("DbgpResponseErrors.properties");
		try {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static enum DbgpError {
		NO_ERROR(0), COMMAND_PARSE_ERROR(1);

		private static final String MESSAGE_NOT_FOUNDED = "Message not founded";
		
		private int code;
		private String message;

		private DbgpError(int code) {
			this.code = code;
		}

		public String getMessage() {
			if (message == null) {
				message = properties.getProperty(code + "",
						MESSAGE_NOT_FOUNDED);
				if (message == null) {
					message = "";
				}
			}
			return message;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(DbgpError.NO_ERROR.getMessage());
		System.out.println(DbgpError.COMMAND_PARSE_ERROR.getMessage());
	}
}
