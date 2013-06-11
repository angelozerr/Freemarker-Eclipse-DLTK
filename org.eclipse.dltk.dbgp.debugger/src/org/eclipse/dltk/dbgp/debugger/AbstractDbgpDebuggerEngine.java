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
package org.eclipse.dltk.dbgp.debugger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.debugger.BreakPointLocation;
import org.eclipse.dltk.dbgp.debugger.debugger.DebuggerState;
import org.eclipse.dltk.dbgp.debugger.debugger.IDebugger;
import org.eclipse.dltk.dbgp.debugger.debugger.event.IDebuggerEvent;
import org.eclipse.dltk.dbgp.debugger.debugger.event.IDebuggerEventListener;
import org.eclipse.dltk.dbgp.debugger.debugger.event.IStreamEvent;
import org.eclipse.dltk.dbgp.debugger.debugger.event.ISuspendedEvent;
import org.eclipse.dltk.dbgp.debugger.internal.packet.receiver.DbgpAsciiPacketReceiver;
import org.eclipse.dltk.dbgp.debugger.internal.packet.receiver.IDbgpCommandListener;
import org.eclipse.dltk.dbgp.debugger.internal.packet.sender.DbgpXmlPacketSender;
import org.eclipse.dltk.dbgp.debugger.packet.sender.DbgpXmlPacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.InitPacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.BreakPointRemovePacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.BreakPointSetPacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.ContextGetPacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.DbgpXmlResponsePacket;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.DefaultDbgpResponseFactory;
import org.eclipse.dltk.dbgp.debugger.packet.sender.response.IDbgpResponseFactory;
import org.eclipse.dltk.dbgp.debugger.packet.sender.status.DefaultDbgpStatusFactory;
import org.eclipse.dltk.dbgp.debugger.packet.sender.status.IDbgpStatusFactory;
import org.eclipse.dltk.dbgp.debugger.packet.sender.stream.StdoutStreamPacket;
import org.eclipse.dltk.dbgp.internal.DbgpTermination;

/**
 * Abstract class DBGp debugger enigne, wich implements several methods of
 * {@link IDbgpDebuggerEngine}.
 * 
 */
public abstract class AbstractDbgpDebuggerEngine extends DbgpTermination
		implements IDbgpDebuggerEngine, IDbgpConstants, IDbgpCommandListener,
		IDebuggerEventListener {

	private final InetAddress ideAdress;
	private final int port;
	private final String ideKey;
	private final URI fileURI;
	private IDbgpResponseFactory responseFactory = DefaultDbgpResponseFactory.INSTANCE;
	private IDbgpStatusFactory statusFactory = DefaultDbgpStatusFactory.INSTANCE;

	private boolean debugDbgpProtocol;

	private DbgpAsciiPacketReceiver receiver;
	private DbgpXmlPacketSender sender;
	private IDebugger debugger;
	private boolean debuggerStarted;

	private DbgpRequest lastContinuationCommand;
	private DbgpRequest lastStackGetCommand;
	private ISuspendedEvent lastSuspendedEvent;
	private Thread thread;
	private boolean forceStop;

	private Collection<DbgpContext> contexts = null;

	public AbstractDbgpDebuggerEngine(InetAddress ideAdress, int port,
			String ideKey, URI fileURI, boolean debugDbgpProtocol) {
		this.ideAdress = ideAdress;
		this.port = port;
		this.ideKey = ideKey;
		this.fileURI = fileURI;
		this.debugDbgpProtocol = debugDbgpProtocol;
		this.contexts = createDbgpContexts();
	}

	public synchronized void start() throws IOException {
		try {
			// Connect to DBGP Client Eclipse IDE
			Socket client = new Socket(ideAdress, port);

			// Receive messages from DBGP Client Eclipse IDE
			receiver = new DbgpAsciiPacketReceiver(this, new DataInputStream(
					client.getInputStream()));
			// Response messages to DBGP Client Eclipse IDE
			sender = new DbgpXmlPacketSender(this, new DataOutputStream(client
					.getOutputStream()));

			// add a listener IDbgpCommandListener to call
			// DbgpFreemarkerDebugger#commandReceived
			// which call notify() to not wait (see wait() call into
			// workingCycle)
			receiver.addCommandListener(this);

			// Create debugger
			debugger = createDebugger();
			if (debugger != null) {
				// debugger.addTerminationListener(this);
				debugger.addDebuggerEventListener(this);
				// debugger.connect();
			}
			// Start receiver to get ASCII messages from the IDE DBGP debugger
			receiver.start();
			// Start sender to send XML messages to the IDE DBGP debugger
			sender.start();

			// Send XML init packet to manage Connection Initialization. See
			// specification at
			// http://xdebug.org/docs-dbgp.php#connection-initialization
			InitPacket initPacket = createInitPacket(ideKey, Thread
					.currentThread().getId()
					+ "", fileURI);
			sender.send(initPacket);

			while (true) {
				try {

					while (!receiver.hasAvailableCommand()) {
						wait();
						DebuggerState state = debugger.getState();
						if (state == DebuggerState.STOPPING
								|| state == DebuggerState.STOPPED) {
							break;
						}
					}

					DbgpRequest request = receiver.retrieveCommand();
					if (request != null) {
						DbgpXmlResponsePacket response = processCommand(request);
						if (response != null) {
							sender.send(response);
						}
					} else {
						DebuggerState state = debugger.getState();
						if (state == DebuggerState.STOPPED)
							break;
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			terminate(e);
			return;
		}
		terminate(null);

	}

	private void terminate(Exception e) {
		if (!forceStop) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		//			
		// }
		while (receiver.hasAvailableCommand()) {

		}
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		//			
		// }
		// while(receiver.hasAvailableCommand()) {
		//			
		// }
		debugger.removeDebuggerEventListener(this);
		thread = null;
		fireObjectTerminated(e);
	}

	public void stop() {
		DebuggerState state = debugger.getState();
		if (state == DebuggerState.STOPPED)
			return;
		internalStop();
	}

	private void internalStop() {
		debugger.stop();
		synchronized (this) {
			notify();
		}
	}

	public void commandReceived() {
		processNext();
	}

	private synchronized void processNext() {
		notify();
	}

	private synchronized DbgpXmlResponsePacket processCommand(
			final DbgpRequest request) {
		String command = request.getCommand();
		if (command.equals(COMMAND_FEATURE_SET)) {
			return responseFactory.processCommandFeatureSet(this, request);
		}
		if (command.equals(COMMAND_FEATURE_GET)) {
			return responseFactory.processCommandFeatureGet(this, request);
		}
		if (command.equals(COMMAND_STDOUT)) {
			return responseFactory.processCommandStdout(this, request);
		}
		if (command.equals(COMMAND_STDERR)) {
			return responseFactory.processCommandStderr(this, request);
		}
		if (command.equals(COMMAND_RUN)) {
			return processCommandRun(request);
		}
		if (command.equals(COMMAND_STOP)) {
			return processCommandStop(request);
		}
		if (command.equals(COMMAND_BREAK)) {
			return processCommandBreak(request);
		}
		if (command.equals(COMMAND_STACK_GET)) {
			return processCommandStackGet(request);
		}
		if (command.equals(COMMAND_BREAKPOINT_SET)) {
			return processCommandBreakPointSet(request);
		}
		if (command.equals(COMMAND_BREAKPOINT_GET)) {
			return responseFactory.processCommandBreakPointGet(this, request);
		}
		if (command.equals(COMMAND_BREAKPOINT_REMOVE)) {
			return processCommandBreakRemove(request);
		}
		if (command.equals(COMMAND_CONTEXT_NAMES)) {
			return responseFactory.processCommandContextNames(this, request);
		}
		if (command.equals(COMMAND_CONTEXT_GET)) {
			return processCommandContextGet(request);

		}
		if (command.equals(COMMAND_STEP_OVER)) {
			return responseFactory.processCommandStepOver(this, request);
		}
		if (command.equals(COMMAND_EVAL)) {
			// TODO : eval
			final int transactionId = Integer.parseInt(request.getOption("-i"));
			debugger.evaluate(request.getData(), transactionId);
		}
		return null;
	}

	private DbgpXmlResponsePacket processCommandBreakPointSet(
			final DbgpRequest request) {
		BreakPointSetPacket packet = responseFactory
				.processCommandBreakPointSet(this, request);
		int lineno = packet.getLineno();
		String filename = packet.getFilename();

		// FIXME : manage error when breakpoint fire error
		String breakPointId = debugger.addBreakPoint(lineno, filename);
		packet.setBreakPointId(breakPointId);
		return packet;
	}

	private DbgpXmlResponsePacket processCommandRun(final DbgpRequest request) {
		lastContinuationCommand = request;
		if (!isDebuggerStarted()) {
			runDebugger();
		} else {
			debugger.resume();
		}
		return null;
	}

	private DbgpXmlResponsePacket processCommandStop(final DbgpRequest request) {
		this.forceStop = true;
		lastContinuationCommand = request;
		debugger.stop();
		fireObjectTerminated(null);
		return null;
	}

	private DbgpXmlResponsePacket processCommandBreak(DbgpRequest request) {
		lastContinuationCommand = request;
		debugger.suspend();
		return responseFactory.processCommandBreak(this, request);
	}

	private DbgpXmlResponsePacket processCommandBreakRemove(DbgpRequest command) {
		BreakPointRemovePacket packet = responseFactory
				.processCommandBreakPointRemove(this, command);
		String breakPointId = packet.getBreakPointId();
		debugger.removeBreakPoint(breakPointId);
		return packet;
	}

	private DbgpXmlResponsePacket processCommandStackGet(DbgpRequest request) {
		lastStackGetCommand = request;
		processStackRequestAndSuspendedMesage();
		return null;
	}

	private DbgpXmlResponsePacket processCommandContextGet(DbgpRequest request) {
		ContextGetPacket packet = responseFactory.processCommandContextGet(
				this, request);
		debugger.collectVariables(packet.getContextId(), packet);
		return packet;

	}

	public void setResponseFactory(IDbgpResponseFactory responseFactory) {
		this.responseFactory = responseFactory;
	}

	public void setStatusFactory(IDbgpStatusFactory statusFactory) {
		this.statusFactory = statusFactory;
	}

	public boolean isTraceDbgpProtocol() {
		return debugDbgpProtocol;
	}

	public void requestTermination() {
		// TODO Auto-generated method stub

	}

	public void waitTerminated() throws InterruptedException {

	}

	// --------------- Debugger

	protected abstract IDebugger createDebugger();

	public IDebugger getDebugger() {
		return debugger;
	}

	public boolean isDebuggerStarted() {
		return debuggerStarted;
	}

	private void runDebugger() {
		if (debugger == null)
			return;

		this.debuggerStarted = true;

		thread = new Thread() {
			public void run() {
				debugger.run();
			};
		};
		thread.start();
	}

	public void handleDebuggerEvent(IDebuggerEvent event) {
		DbgpXmlPacket packet = null;
		IDebuggerEvent.Type type = event.getType();
		switch (type) {

		case STDOUT_STREAM:
			String out = ((IStreamEvent) event).getContent();
			packet = new StdoutStreamPacket(out);
			break;

		case STDERR_STREAM:
			String err = ((IStreamEvent) event).getContent();
			packet = new StdoutStreamPacket(err);
			break;

		case RUNNING:
			// Do nothing
			break;

		case SUSPENDED:
			// Debugger is suspended, send packet
			ISuspendedEvent sm = (ISuspendedEvent) event;
			lastSuspendedEvent = sm;
			processStackRequestAndSuspendedMesage();
			packet = statusFactory.processStatusBreak(this,
					lastContinuationCommand, sm);
			break;

		case STOPPING:
			// Do nothing
			break;

		case STOPPED:

			// try {
			// if (debugDbgpProtocol) {
			// System.out.println("Waiting for debugger");
			// }
			// debugger.waitTerminated();
			// if (debugDbgpProtocol) {
			// System.out.println("waited for debugger");
			// }
			// } catch (InterruptedException e1) {
			// e1.printStackTrace();
			// }

			synchronized (this) {
				notify();
			}
			break;
		}

		if (packet != null) {
			sender.send(packet);
		}
	}

	// --------------- DBGp Init Packet

	/**
	 * 
	 * @param ideKey
	 * @param threadId
	 * @param fileURI
	 * @return
	 */
	protected InitPacket createInitPacket(String ideKey, String threadId,
			URI fileURI) {
		String appid = getInitPacketAppid();
		String session = getInitPacketSession();
		String parent = getInitPacketParent();
		String language = getInitPacketLanguage();
		String protocolVersion = getInitPacketProtocolVersion();
		return new InitPacket(appid, ideKey, session, threadId, parent,
				language, protocolVersion, fileURI);
	}

	private String getInitPacketSession() {
		return null;
	}

	protected String getInitPacketParent() {
		return "";
	}

	protected abstract String getInitPacketAppid();

	protected abstract String getInitPacketLanguage();

	protected abstract String getInitPacketProtocolVersion();

	public boolean isTerminated() {
		DebuggerState state = debugger.getState();
		return state == DebuggerState.STOPPED;
	}

	public URI getFileURI() {
		return fileURI;
	}

	private synchronized void processStackRequestAndSuspendedMesage() {
		if (lastStackGetCommand == null || lastSuspendedEvent == null) {
			return;
		}

		BreakPointLocation location = lastSuspendedEvent.getLocation();

		// send an answer to the stack_get command

		DbgpXmlResponsePacket response = responseFactory
				.processCommandStackGet(this, lastStackGetCommand, location);

		sender.send(response);

		lastSuspendedEvent = null;
		lastStackGetCommand = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine#getDbgpContexts()
	 */
	public final Collection<DbgpContext> getDbgpContexts() {
		return contexts;
	}

	/**
	 * By default none DBGp context is supported.
	 */
	protected Collection<DbgpContext> createDbgpContexts() {
		return Collections.emptyList();
	}

}
