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

import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.debugger.event.IDebuggerEvent;
import org.eclipse.dltk.dbgp.debugger.debugger.event.IDebuggerEventListener;
import org.eclipse.dltk.dbgp.debugger.debugger.event.ISuspendedEvent;
import org.eclipse.dltk.dbgp.debugger.debugger.event.ISuspendedEvent.SuspendedCause;
import org.eclipse.dltk.dbgp.debugger.internal.debugger.event.RunningEvent;
import org.eclipse.dltk.dbgp.debugger.internal.debugger.event.StartingEvent;
import org.eclipse.dltk.dbgp.debugger.internal.debugger.event.StderrStreamEvent;
import org.eclipse.dltk.dbgp.debugger.internal.debugger.event.StdoutStreamEvent;
import org.eclipse.dltk.dbgp.debugger.internal.debugger.event.StoppedEvent;
import org.eclipse.dltk.dbgp.debugger.internal.debugger.event.StoppingEvent;
import org.eclipse.dltk.dbgp.debugger.internal.debugger.event.SuspendedEvent;

/**
 * Abstract class for debugger.
 * 
 * @param <T>
 *            native breakpoint.
 */
public abstract class AbstractDebugger<T> implements IDebugger {

	// State of the debugger
	private DebuggerState state = DebuggerState.INITIAL;

	// store debugger event listener
	private ListenerList eventListeners = new ListenerList(
			ListenerList.IDENTITY);

	// Native BreakPoint registered into map to retrieve it with breakPointId.
	private Map<String, T> breakPoints = null;

	private StringWriter writer = null;

	private URI fileURI;

	private String fileURIBaseDir;

	public AbstractDebugger(IDbgpDebuggerEngine debuggerEngine) {
		this.fileURI = debuggerEngine.getFileURI();
	}

	/**
	 * Run the debugger.
	 */
	public final void run() {
		// running the debugger...
		setState(DebuggerState.RUNNING);
		try {
			// run the debugger
			doRun();
		} finally {
			// stop the debugger...
			stop();
		}
	}

	/**
	 * Stop the debugger.
	 */
	public final void stop() {
		// stopping the debugger
		setState(DebuggerState.STOPPING);
		try {
			// stop the debugger
			doStop();
			// flush writer
			flushWriter();
		} finally {
			// stopped
			setState(DebuggerState.STOPPED);
		}
	}

	/**
	 * Add debuger listener to observe change of state of the debugger.s
	 */
	public void addDebuggerEventListener(IDebuggerEventListener listener) {
		eventListeners.add(listener);
	}

	/**
	 * Remove debugger listener.
	 */
	public void removeDebuggerEventListener(IDebuggerEventListener listener) {
		eventListeners.remove(listener);
	}

	/**
	 * Notify listeners that a state change.
	 * 
	 * @param event
	 */
	protected void notifyListeners(IDebuggerEvent event) {
		Object[] listeners = eventListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			((IDebuggerEventListener) listeners[i]).handleDebuggerEvent(event);
		}
	}

	/**
	 * Fire suspended event.
	 * 
	 * @param cause
	 * @param location
	 */
	protected void fireSuspendedEvent(SuspendedCause cause,
			BreakPointLocation location) {
		notifyListeners(createSuspendedEvent(cause, location));
	}

	/**
	 * Create suspended event.
	 * 
	 * @param cause
	 * @param location
	 * @return
	 */
	private ISuspendedEvent createSuspendedEvent(SuspendedCause cause,
			BreakPointLocation location) {
		return new SuspendedEvent(cause, location);
	}

	/**
	 * Update the state of the debugger and fire event.
	 * 
	 * @param state
	 */
	private void setState(DebuggerState state) {
		this.state = state;
		switch (state) {
		case STARTING:
			notifyListeners(StartingEvent.INSTANCE);
			break;
		case RUNNING:
			notifyListeners(RunningEvent.INSTANCE);
			break;
		case STOPPING:
			notifyListeners(StoppingEvent.INSTANCE);
			break;
		case STOPPED:
			notifyListeners(StoppedEvent.INSTANCE);
			break;
		}
	}

	/**
	 * Display content into Eclipse Console.
	 * 
	 * @param content
	 */
	protected void out(String content) {
		IDebuggerEvent event = new StdoutStreamEvent(content);
		notifyListeners(event);
	}

	/**
	 * Display exception into Eclipse Console.
	 * 
	 * @param e
	 */
	protected void err(Throwable e) {
		IDebuggerEvent event = new StderrStreamEvent(e);
		notifyListeners(event);
	}

	/**
	 * Add native BreakPoint and return the BreakPoint ID.
	 */
	public String addBreakPoint(int lineno, String filename) {
		T breakPoint = createBreakpoint(filename, lineno);
		String breakPointID = this.registerBreakPoint(breakPoint);
		return breakPointID;
	}

	/**
	 * Remove the native BreakPoint retrieved by BreakPoint ID.
	 */
	public void removeBreakPoint(String breakPointId) {
		T breakPoint = this.getBreakPoint(breakPointId);
		if (breakPoint != null) {
			removeBreakpoint(breakPoint);
		}
	}

	/**
	 * Register the native breakpoint into a Map and return it the ID of this
	 * breakpoint.
	 * 
	 * @param breakPoint
	 * @return
	 */
	protected String registerBreakPoint(T breakPoint) {
		if (breakPoints == null) {
			breakPoints = new HashMap<String, T>();
		}

		String breakPointID = breakPoint.hashCode() + "";
		breakPoints.put(breakPointID, breakPoint);
		return breakPointID;
	}

	/**
	 * Return the native breakpoint retrieved by breakpointId and null
	 * otherwise.
	 * 
	 * @param breakPointId
	 * @return
	 */
	protected T getBreakPoint(String breakPointId) {
		if (breakPoints == null)
			return null;
		return breakPoints.get(breakPointId);
	}

	/**
	 * Returns the sate of the debugger.
	 */
	public DebuggerState getState() {
		return state;
	}

	/**
	 * Suspend the debugger due to a breakpoint.
	 * 
	 * @param fileName
	 * @param lineBegin
	 * @param columnBegin
	 * @param lineEnd
	 * @param columnEnd
	 */
	protected void suspendByBreakPoint(String fileName, int lineBegin,
			int columnBegin, int lineEnd, int columnEnd) {

		BreakPointLocation location = new BreakPointLocation(fileName,
				lineBegin, columnBegin, lineEnd, columnEnd);
		fireSuspendedEvent(SuspendedCause.BREAKPOINT, location);

		flushWriter();
	}

	/**
	 * Flush the writer.
	 */
	private void flushWriter() {
		if (writer != null) {
			out(writer.toString());
			writer.getBuffer().setLength(0);
		}
	}

	/**
	 * Returns the writer.
	 * 
	 * @return
	 */
	protected Writer getWriter() {
		if (writer == null) {
			writer = new StringWriter();
		}
		return writer;
	}

	/**
	 * Implement run debugger.
	 */
	protected abstract void doRun();

	/**
	 * Implement stop debugger.
	 */
	protected abstract void doStop();

	/**
	 * Create native breakpoint.
	 * 
	 * @param filename
	 * @param lineno
	 * @return
	 */
	protected abstract T createBreakpoint(String filename, int lineno);

	/**
	 * Remove native breakpoint.
	 * 
	 * @param breakPoint
	 */
	protected abstract void removeBreakpoint(T breakPoint);

	protected URI getFileURI() {
		return fileURI;
	}

	/**
	 * Return base dir of the fileURI.
	 * 
	 * @return
	 */
	public String getFileURIBaseDir() {
		if (fileURIBaseDir == null) {
			this.fileURIBaseDir = fileURI.getPath();
			int lastIndex = fileURIBaseDir.lastIndexOf("/");
			if (lastIndex == -1) {
				lastIndex = fileURIBaseDir.lastIndexOf("\\");
			}
			if (lastIndex != -1) {
				fileURIBaseDir = fileURIBaseDir.substring(0, lastIndex + 1);
			}
			String sheme = fileURI.getScheme();
			if (sheme != null && sheme.startsWith("file")) {
				this.fileURIBaseDir = "file://" + this.fileURIBaseDir;
			}
		}
		return fileURIBaseDir;
	}

	/**
	 * Return relative path of the filename.
	 * 
	 * @param fileName
	 * @return
	 */
	protected String getRelativePath(String fileName) {
		String fileURIBaseDir = getFileURIBaseDir();
		return fileName.substring(fileURIBaseDir.length(), fileName.length());
	}

}
