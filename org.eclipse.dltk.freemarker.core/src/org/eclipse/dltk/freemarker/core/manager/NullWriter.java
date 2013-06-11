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
package org.eclipse.dltk.freemarker.core.manager;

import java.io.Writer;

/**
 * This {@link Writer} writes all data to the famous <b>/dev/null</b>.
 * <p>
 * This <code>Writer</code> has no destination (file/socket etc.) and all
 * characters written to it are ignored and lost.
 * 
 * @version $Id: NullWriter.java 610010 2008-01-08 14:50:59Z niallp $
 */
public class NullWriter extends Writer {

	/**
	 * A singleton.
	 */
	public static final NullWriter NULL_WRITER = new NullWriter();

	/**
	 * Constructs a new NullWriter.
	 */
	public NullWriter() {
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param idx
	 *            The character to write
	 */
	public void write(int idx) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param chr
	 *            The characters to write
	 */
	public void write(char[] chr) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param chr
	 *            The characters to write
	 * @param st
	 *            The start offset
	 * @param end
	 *            The number of characters to write
	 */
	public void write(char[] chr, int st, int end) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param str
	 *            The string to write
	 */
	public void write(String str) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param str
	 *            The string to write
	 * @param st
	 *            The start offset
	 * @param end
	 *            The number of characters to write
	 */
	public void write(String str, int st, int end) {
		// to /dev/null
	}

	/** @see java.io.Writer#flush() */
	public void flush() {
		// to /dev/null
	}

	/** @see java.io.Writer#close() */
	public void close() {
		// to /dev/null
	}

}
