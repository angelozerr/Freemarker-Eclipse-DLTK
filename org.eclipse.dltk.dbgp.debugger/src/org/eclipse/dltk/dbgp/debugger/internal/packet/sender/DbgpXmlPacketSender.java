package org.eclipse.dltk.dbgp.debugger.internal.packet.sender;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.packet.sender.DbgpXmlPacket;
import org.eclipse.dltk.dbgp.internal.DbgpWorkingThread;
import org.eclipse.dltk.dbgp.internal.IDbgpTerminationListener;

public class DbgpXmlPacketSender extends DbgpWorkingThread implements
		IDbgpTerminationListener {

	private static final String THREAD_NAME = "DBGP Debugger - XML Packet sender";
	
	final private OutputStream outputStream;
	final private Queue<DbgpXmlPacket> fResponseQueue = new ConcurrentLinkedQueue<DbgpXmlPacket>();

	private final Object fTerminatedLock = new Object();
	private boolean fTerminated = false;

	private IDbgpDebuggerEngine engine;

	public DbgpXmlPacketSender(IDbgpDebuggerEngine engine,
			OutputStream outputStream) {
		super(THREAD_NAME);
		this.outputStream = outputStream;
		this.engine = engine;
		this.engine.addTerminationListener(this);
	}

	protected void workingCycle() throws Exception {
		try {
			while (!hasTerminated() || !fResponseQueue.isEmpty()) {
				while (fResponseQueue.isEmpty()) {
					Thread.sleep(150);
					if (hasTerminated()) {
						return;
					}
				}
				
				DbgpXmlPacket xe = fResponseQueue.peek();
				String data = xe.toXml();
				String length = "" + data.length();

				outputStream.write(length.getBytes());
				outputStream.write(0);
				outputStream.write(data.getBytes());
				outputStream.write(0);
				outputStream.flush();
				if (engine.isTraceDbgpProtocol()) {
					System.out.println("sent: " + data);
				}				
				fResponseQueue.remove(xe);

			}
		} catch (InterruptedException ie) {
			if (engine.isTraceDbgpProtocol()) {
				System.err.println("Sender exception: " + ie.getMessage());
				System.out.println("Sender exception: terminating responder");
			}
		} catch (IOException ioe) {
			if (engine.isTraceDbgpProtocol()) {
				System.err.println("Sender exception: " + ioe.getMessage());
				System.out.println("Sender exception: terminating responder");
			}
		}
	}

	public boolean hasAvailableResponse() {
		return !fResponseQueue.isEmpty();
	}
	
	private boolean hasTerminated() {
		return fTerminated;
	}

	public void terminate() {
		fTerminated = true;
	}

	public void send(DbgpXmlPacket response) {
		if (!fTerminated) {
			fResponseQueue.add(response);
		}
	}

	public void objectTerminated(Object object, Exception e) {
		synchronized (fTerminatedLock) {
			if (fTerminated) {
				return;
			}

			try {
				outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			engine.removeTerminationListener(this);
			try {
				engine.waitTerminated();
			} catch (InterruptedException e1) {
				// OK, interrupted
			}

			fTerminated = true;
		}

		fireObjectTerminated(e);
	}

}
