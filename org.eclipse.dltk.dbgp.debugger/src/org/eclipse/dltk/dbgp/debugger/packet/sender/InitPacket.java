package org.eclipse.dltk.dbgp.debugger.packet.sender;

import java.net.URI;

/**
 * DBGp XML Init Packet. Example :
 * 
 * <pre>
 * <init appid="APPID"
 *       idekey="IDE_KEY"
 *       session="DBGP_COOKIE"
 *       thread="THREAD_ID"
 *       parent="PARENT_APPID"
 *       language="LANGUAGE_NAME"
 *       protocol_version="1.0"
 *       fileuri="file://path/to/file">
 * </pre>
 * 
 * @see specification at
 *      http://xdebug.org/docs-dbgp.php#connection-initialization
 */
public class InitPacket extends DbgpXmlPacket {

	private static final String INIT_ATTR = "init";
	private static final String APPID_ATTR = "appid";
	private static final String IDEKEY_ATTR = "idekey";
	private static final String SESSION_ATTR = "session";
	private static final String THREAD_ATTR = "thread";
	private static final String PARENT_ATTR = "parent";
	private static final String LANGUAGE_ATTR = "language";
	private static final String PROTOCOL_VERSION_ATTR = "protocol_version";
	private static final String FILEURI_ATTR = "fileuri";

	/**
	 * 
	 * @param appid
	 *            defined by the debugger engine
	 * @param ideKey
	 *            defined by the user. The DBGP_IDEKEY environment variable
	 *            SHOULD be used if it is available, otherwise setting this
	 *            value is debugger engine implementation specific. This value
	 *            may be empty.
	 * @param session
	 *            If the environment variable DBGP_COOKIE exists, then the init
	 *            packet MUST contain a session attribute with the value of the
	 *            variable. This allows an IDE to execute a debugger engine, and
	 *            maintain some state information between the execution and the
	 *            protocol connection. This value should not be expected to be
	 *            set in 'remote' debugging situations where the IDE is not in
	 *            control of the process.
	 * @param threadId
	 *            the systems thread id
	 * @param parent
	 *            the appid of the application that spawned the process. When an
	 *            application is executed, it should set it's APPID into the
	 *            environment. If an APPID already exists, it should first read
	 *            that value and use it as the PARENT_APPID.
	 * @param language
	 *            debugger engine specific, must not contain additional
	 *            information, such as version, etc.
	 * @param protocolVersion
	 *            The highest version of this protocol supported * @param
	 *            fileURI URI of the script file being debugged
	 */
	public InitPacket(String appid, String ideKey, String session,
			String threadId, String parent, String language,
			String protocolVersion, URI fileURI) {
		super(INIT_ATTR);
		addAttribute(APPID_ATTR, appid);
		addAttribute(IDEKEY_ATTR, ideKey);
		if (session != null) {
			addAttribute(SESSION_ATTR, session);
		}
		addAttribute(THREAD_ATTR, threadId);
		addAttribute(PARENT_ATTR, parent);
		addAttribute(LANGUAGE_ATTR, language);
		addAttribute(FILEURI_ATTR, fileURI.toString());
		addAttribute(PROTOCOL_VERSION_ATTR, protocolVersion);
	}
}
