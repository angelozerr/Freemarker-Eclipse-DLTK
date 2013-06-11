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
package org.eclipse.dltk.dbgp.debugger.packet.sender;

/**
 * Base Dbgp XML Packet.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.dbgp.internal.utils.Base64Helper;

public class DbgpXmlPacket {

	private String name;
	private Map<String, String> attributes = new HashMap<String, String>();
	private String data;
	private boolean encodeData = true;
	private List<DbgpXmlPacket> elements = null;

	public DbgpXmlPacket(String name) {
		this.name = name;
	}

	public void addAttribute(String name, boolean value) {
		addAttribute(name, value + "");
	}

	public void addAttribute(String name, int value) {
		addAttribute(name, value + "");
	}

	public void addAttribute(String name, String value) {
		attributes.put(name, value);
	}

	public String getAttribute(String name) {
		return attributes.get(name);
	}

	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		setData(data, false);
	}

	public void setData(String data, boolean encode) {
		this.data = data;
		encodeData = encode;
	}

	public String toXml() {
		StringBuffer xml = new StringBuffer();
		elementToXml(this, xml);
		return xml.toString();
	}

	@Override
	public String toString() {
		return toXml();
	}

	private static void elementToXml(DbgpXmlPacket packet, StringBuffer xml) {
		xml.append("<");
		xml.append(packet.name);
		attributesToXml(packet.attributes, xml);

		if (packet.data == null && packet.elements == null) {
			xml.append(" />");
		} else {
			xml.append(" >");
			if (packet.data != null) {
				xml.append(packet.prepareData(packet.data));
			}
			if (packet.elements != null) {
				for (DbgpXmlPacket child : packet.elements) {
					elementToXml(child, xml);
				}
			}
			xml.append("</");
			xml.append(packet.name);
			xml.append(">");
		}
	}

	private static void attributesToXml(Map<String, String> attributes,
			StringBuffer xml) {
		for (String name : attributes.keySet()) {
			xml.append(" ");
			xml.append(name);
			xml.append("=\"");
			xml.append(attributes.get(name));
			xml.append("\"");
		}
	}

	private String prepareData(String fData) {
		if (encodeData) {
			String encoded = Base64Helper.encodeString(fData);
			return "<![CDATA[" + encoded + "]]>";
		}
		return fData;
	}

	protected void addElement(DbgpXmlPacket packet) {
		if (elements == null) {
			elements = new ArrayList<DbgpXmlPacket>();
		}
		elements.add(packet);
	}

	protected DbgpXmlPacket addElement(String name) {
		DbgpXmlPacket packet = new DbgpXmlPacket(name);
		addElement(packet);
		return packet;
	}
}
