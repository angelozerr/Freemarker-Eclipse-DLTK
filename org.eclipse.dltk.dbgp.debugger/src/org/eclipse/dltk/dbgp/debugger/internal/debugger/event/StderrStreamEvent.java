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
package org.eclipse.dltk.dbgp.debugger.internal.debugger.event;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.dltk.dbgp.debugger.debugger.event.IStreamEvent;

/**
 * Stderr debugger event.
 * 
 */
public class StderrStreamEvent extends AbstractDebuggerEvent implements IStreamEvent {

	private String content;

	public StderrStreamEvent(Throwable e) {
		super(Type.STDERR_STREAM);
		// Get string of the exception
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);		
		e.printStackTrace(writer);
		writer.flush();
		writer.close();		
		this.content = stringWriter.getBuffer().toString();
	}

	public String getContent() {
		return content;
	}

}
