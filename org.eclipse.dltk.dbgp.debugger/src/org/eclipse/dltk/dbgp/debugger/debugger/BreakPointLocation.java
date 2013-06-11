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
package org.eclipse.dltk.dbgp.debugger.debugger;

/**
 * Breakpoint location.
 * 
 */
public class BreakPointLocation {

	private String fileName;
	private int lineBegin;
	private int columnBegin;
	private int lineEnd;
	private int columnEnd;

	protected BreakPointLocation() {
	}

	public BreakPointLocation(String fileName, int lineBegin, int columnBegin,
			int lineEnd, int columnEnd) {
		this.fileName = fileName;
		this.lineBegin = lineBegin;
		this.columnBegin = columnBegin;
		this.lineEnd = lineEnd;
		this.columnEnd = columnEnd;
	}

	public String getFileName() {
		return fileName;
	}

	public int getLineBegin() {
		return lineBegin;
	}

	public int getColumnBegin() {
		return columnBegin;
	}

	public int getLineEnd() {
		return lineEnd;
	}

	public int getColumnEnd() {
		return columnEnd;
	}
}
