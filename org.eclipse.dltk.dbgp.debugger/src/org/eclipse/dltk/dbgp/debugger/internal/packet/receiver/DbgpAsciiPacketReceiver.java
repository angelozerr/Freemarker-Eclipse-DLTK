package org.eclipse.dltk.dbgp.debugger.internal.packet.receiver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.dbgp.internal.DbgpWorkingThread;
import org.eclipse.dltk.dbgp.internal.IDbgpTerminationListener;

public class DbgpAsciiPacketReceiver extends DbgpWorkingThread implements
		IDbgpTerminationListener {

	private static final String THREAD_NAME = "DBGP Debugger - ASCII Packet receiver";
	
	final private InputStream inputStream;
	final private Queue<DbgpRequest> commandQueue = new ConcurrentLinkedQueue<DbgpRequest>();

	List<IDbgpCommandListener> listeners = new ArrayList<IDbgpCommandListener>(
			1);

	private final Object terminatedLock = new Object();
	private boolean terminated = false;

	private IDbgpDebuggerEngine engine;

	public DbgpAsciiPacketReceiver(IDbgpDebuggerEngine engine,
			InputStream inputStream) {
		super(THREAD_NAME);
		this.inputStream = inputStream;

		this.engine = engine;
		this.engine.addTerminationListener(this);
	}

	protected void workingCycle() throws Exception {
		try {
			while (!terminated) {
				StringBuffer sb = new StringBuffer();
				int c = inputStream.read();
				while (c != -1 && c != 0) {
					sb.append((char) c);
					c = inputStream.read();
				}
				if (c == 0) {
					enqueueCommand(DbgpRequestParser.parse(sb.toString()));
				}
			}
		} catch (IOException ioe) {
			if (engine.isTraceDbgpProtocol()) {
				System.err.println("Receiver exception: " + ioe.getMessage());
				System.out.println("Receiver exception: terminating receiver");
			}
		} catch (DbgpException de) {
			if (engine.isTraceDbgpProtocol()) {
				System.err.println("Receiver exception: " + de.getMessage());
				System.out.println("Receiver exception: terminating receiver");
			}
		}
	}

	public DbgpRequest retrieveCommand() {
		return commandQueue.poll();
	}

	public boolean hasAvailableCommand() {
		return !commandQueue.isEmpty();
	}

	private void enqueueCommand(DbgpRequest request) {
		commandQueue.offer(request);
		if (engine.isTraceDbgpProtocol()) {
			System.out.println("enqueued: " + request.toString());
		}
		notifyListeners();
	}

	private void notifyListeners() {
		for (IDbgpCommandListener listener : listeners) {
			listener.commandReceived();
		}
	}

	public void addCommandListener(IDbgpCommandListener listener) {
		listeners.add(listener);
	}

	public void removeCommandListener(IDbgpCommandListener listener) {
		listeners.remove(listener);
	}

	public void objectTerminated(Object object, Exception e) {
		synchronized (terminatedLock) {
			if (terminated) {
				return;
			}

			try {
				inputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			engine.removeTerminationListener(this);
			try {
				engine.waitTerminated();
			} catch (InterruptedException e1) {
				// OK, interrupted
			}

			terminated = true;
		}

		fireObjectTerminated(e);
	}

}
