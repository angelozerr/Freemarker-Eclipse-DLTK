/*******************************************************************************
 * Copyright (c) 2010 Freemarker Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.dbgp.debugger.packet.sender.stream;

/**
 * Stdout Stream Dbgp XML Packet.
 * 
 * <pre>
 * 	<stream type="stdout" ><![CDATA[CSJIZWxsbyEiDQo=]]></stream>
 * </pre>
 * 
 * @see specification
 *      http://xdebug.org/docs-dbgp.php#debugger-engine-to-ide-communications
 * 
 */
public class StdoutStreamPacket extends StreamPacket {

	public StdoutStreamPacket(String content) {
		super(Type.STDOUT, content);
	}

}
