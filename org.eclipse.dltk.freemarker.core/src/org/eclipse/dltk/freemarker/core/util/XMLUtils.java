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
package org.eclipse.dltk.freemarker.core.util;

import static org.eclipse.dltk.freemarker.core.util.StringUtils.isEmpty;

import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;

/**
 * XML Utilities.
 */
public class XMLUtils {

	/**
	 * Start XML element. Ex : <elementName
	 * 
	 * @param elementName
	 * @param writer
	 * @throws IOException
	 */
	public static void startElement(String elementName, Writer writer)
			throws IOException {
		startElement(elementName, false, writer);
	}

	/**
	 * Start XML element and close it. Ex : <elementName>
	 * 
	 * @param elementName
	 * @param endTag
	 * @param writer
	 * @throws IOException
	 */
	public static void startElement(String elementName, boolean endTag,
			Writer writer) throws IOException {
		writer.write("<");
		writer.write(elementName);
		if (endTag) {
			writer.write(">");
		}
	}

	/**
	 * End XML element. Ex : </elementName>
	 * 
	 * @param elementName
	 * @param writer
	 * @throws IOException
	 */
	public static void endElement(String elementName, Writer writer)
			throws IOException {
		writer.write("</");
		writer.write(elementName);
		writer.write(">");
	}

	/**
	 * End XML element. Ex : />
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public static void endElement(Writer writer) throws IOException {
		writer.write("/>");
	}

	/**
	 * Add long XML attribute value. Ex: name="1"
	 * 
	 * @param name
	 * @param value
	 * @param writer
	 * @throws IOException
	 */
	public static void addAttribute(String name, long value, Writer writer)
			throws IOException {
		addAttribute(name, Long.toString(value), writer);
	}

	/**
	 * Add boolean XML attribute value. Ex : name="true".
	 * 
	 * @param name
	 * @param value
	 * @param writer
	 * @throws IOException
	 */
	public static void addAttribute(String name, boolean value, Writer writer)
			throws IOException {
		addAttribute(name, Boolean.toString(value), writer);
	}

	/**
	 * Add String XML attribute value. Ex : name="bla bla bla".
	 * 
	 * @param name
	 * @param value
	 * @param writer
	 * @throws IOException
	 */
	public static void addAttribute(String name, String value, Writer writer)
			throws IOException {
		if (isEmpty(value))
			return;
		writer.write(" ");
		writer.write(name);
		writer.write("=\"");
		writer.write(getEscaped(value));
		writer.write("\"");
	}

	/**
	 * Escape the String value to returns attribute valid value.
	 * 
	 * @param s
	 * @return
	 */
	private static String getEscaped(String s) {
		StringBuffer result = new StringBuffer(s.length() + 10);
		for (int i = 0; i < s.length(); ++i)
			appendEscapedChar(result, s.charAt(i));
		return result.toString();
	}

	private static void appendEscapedChar(StringBuffer buffer, char c) {
		String replacement = getReplacement(c);
		if (replacement != null) {
			buffer.append('&');
			buffer.append(replacement);
			buffer.append(';');
		} else {
			buffer.append(c);
		}
	}

	private static String getReplacement(char c) {
		// Encode special XML characters into the equivalent character
		// references.
		// These five are defined by default for all XML documents.
		switch (c) {
		case '<':
			return "lt"; //$NON-NLS-1$
		case '>':
			return "gt"; //$NON-NLS-1$
		case '"':
			return "quot"; //$NON-NLS-1$
		case '\'':
			return "apos"; //$NON-NLS-1$
		case '&':
			return "amp"; //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Returns long value of the attribute name.
	 * 
	 * @param name
	 * @param attributes
	 * @return
	 */
	public static Long getLong(String name, Attributes attributes) {
		if (attributes == null)
			return null;
		return StringUtils.getLong(attributes.getValue(name));

	}

}
