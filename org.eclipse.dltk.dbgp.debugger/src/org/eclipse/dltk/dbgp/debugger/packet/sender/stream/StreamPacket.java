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

import org.eclipse.dltk.dbgp.debugger.packet.sender.DbgpXmlPacket;

/**
 * Base Stream Dbgp XML Packet.
 * 
 * @see specification
 *      http://xdebug.org/docs-dbgp.php#debugger-engine-to-ide-communications
 */
public class StreamPacket extends DbgpXmlPacket {

	private static final String STREAM_ELT = "stream";
	private static final String TYPE_ATTR = "type";

	protected enum Type {
		STDERR, STDOUT,
	}

	public StreamPacket(Type type, String content) {
		super(STREAM_ELT);
		super.addAttribute(TYPE_ATTR, type.name().toLowerCase());
		super.setData(content, true);
	}
}
