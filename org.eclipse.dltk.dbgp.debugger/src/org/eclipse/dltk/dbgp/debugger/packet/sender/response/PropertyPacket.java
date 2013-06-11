package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.debugger.packet.sender.DbgpXmlPacket;

/**
 * <pre>
 * <property
 *     name="short_name"
 *     fullname="long_name"
 *     type="data_type"
 *     classname="name_of_object_class"
 *     constant="0|1"
 *     children="0|1"
 *     size="{NUM}"
 *     page="{NUM}"
 *     pagesize="{NUM}"
 *     address="{NUM}"
 *     key="language_dependent_key"
 *     encoding="base64|none"
 *     numchildren="{NUM}">
 * ...encoded Value Data...
 * </property>
 * </pre>
 * 
 */
public class PropertyPacket extends DbgpXmlPacket {

	private static final String PROPERTY_ELT = "property";

	public PropertyPacket(String name, String type, String value) {
		super(PROPERTY_ELT);
		super.addAttribute("name", name);
		super.addAttribute("fullname", name);		
		super.addAttribute("type", type);
		super.addAttribute("constant", "0");
		super.addAttribute("encoding", "base64");		
        setData(value, true);
	}

}
