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
package org.eclipse.dltk.launching;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.dltk.internal.launching.InputStreamWrapper;
import org.eclipse.dltk.internal.launching.NullOutputStream;

/**
 * This abstract class implements Java Lang {@link Process} to emulate a Java
 * Process by implementing {@link RunnableProcess#run()} .
 * 
 * By implementing this class, you can use it with Eclipse
 * {@link RuntimeProcess}.
 * 
 * This class could interest DLTK project?
 * 
 */
public abstract class RunnableProcess extends Process implements Runnable {

	// Thread count used to generate Thread name.
	private static int threadCount = 0;

	// Possible states for a RunnableProcess
	enum RunnableState {
		INITIAL, RUNNING, TERMINATED
	}

	// InputStream Delegate for Process#getInputStream()
	private final InputStreamWrapper inputStreamDelegate = new InputStreamWrapper();
	// InputStream Delegate for Process#getErrorStream()
	private final InputStreamWrapper errorStreamDelegate = new InputStreamWrapper();
	// State of runnable process
	private RunnableState state = RunnableState.INITIAL;
	private Thread thread = null;

	public RunnableProcess() {
		this(null, true);
	}

	/**
	 * Runnable process Constructor.
	 * 
	 * @param label
	 *            of the Thread. If null, Thread name will be generated.
	 * @param lazyStart
	 */
	public RunnableProcess(final String label, boolean lazyStart) {
		String threadName = ((label != null && label.length() > 0) ? label
				: generateThreadName());
		incrementThreadCount();
		this.thread = createThread(createRunnable(), threadName);
		if (lazyStart) {
			start();
		}
	}

	/**
	 * causes the current thread to wait, if necessary, until the process
	 * represented by this <code>Process</code> object has terminated. This
	 * method returns immediately if the subprocess has already terminated. If
	 * the subprocess has not yet terminated, the calling thread will be blocked
	 * until the subprocess exits.
	 * 
	 * @return the exit value of the process. By convention, <code>0</code>
	 *         indicates normal termination.
	 * @exception InterruptedException
	 *                if the current thread is {@linkplain Thread#interrupt()
	 *                interrupted} by another thread while it is waiting, then
	 *                the wait is ended and an {@link InterruptedException} is
	 *                thrown.
	 */
	@Override
	public synchronized int waitFor() throws InterruptedException {
		while (state != RunnableState.TERMINATED) {
			wait();
		}
		return 0;
	}

	/**
	 * Returns the exit value for the subprocess.
	 * 
	 * @return the exit value of the subprocess represented by this
	 *         <code>Process</code> object. by convention, the value
	 *         <code>0</code> indicates normal termination.
	 * @exception IllegalThreadStateException
	 *                if the subprocess represented by this <code>Process</code>
	 *                object has not yet terminated.
	 */
	@Override
	public synchronized int exitValue() {
		if (state != RunnableState.TERMINATED) {
			// According to Process javadoc, if process is not terminated,
			// IllegalThreadStateException must be throwed.
			throw new IllegalThreadStateException("process hasn't exited");
		}
		return 0;
	}

	/**
	 * Gets the output stream of the subprocess. Output to the stream is piped
	 * into the standard input stream of the process represented by this
	 * <code>Process</code> object.
	 * <p>
	 * Implementation note: It is a good idea for the output stream to be
	 * buffered.
	 * 
	 * @return the output stream connected to the normal input of the
	 *         subprocess.
	 */
	@Override
	public OutputStream getOutputStream() {
		return NullOutputStream.INSTANCE;
	}

	/**
	 * Gets the input stream of the subprocess. The stream obtains data piped
	 * from the standard output stream of the process represented by this
	 * <code>Process</code> object.
	 * <p>
	 * Implementation note: It is a good idea for the input stream to be
	 * buffered.
	 * 
	 * @return the input stream connected to the normal output of the
	 *         subprocess.
	 * @see ProcessBuilder#redirectErrorStream()
	 */

	@Override
	public InputStream getInputStream() {
		return inputStreamDelegate;
	}

	/**
	 * Gets the error stream of the subprocess. The stream obtains data piped
	 * from the error output stream of the process represented by this
	 * <code>Process</code> object.
	 * <p>
	 * Implementation note: It is a good idea for the input stream to be
	 * buffered.
	 * 
	 * @return the input stream connected to the error stream of the subprocess.
	 * @see ProcessBuilder#redirectErrorStream()
	 */
	@Override
	public InputStream getErrorStream() {
		return errorStreamDelegate;
	}

	/**
	 * Kills the subprocess. The subprocess represented by this
	 * <code>Process</code> object is forcibly terminated.
	 */
	@Override
	public void destroy() {		
		if (isTerminated())
			return;		
		try {
			terminate();
			
		} finally {
			if (thread != null && thread.isAlive()) {
				// FIXME : // when runnable loop for while(true) {} Thread is never killed.
				thread.interrupt();
			}
			thread = null;
		}
	}

	// ------------- out/err

	/**
	 * Write string into {@link Process#getInputStream()}. This method must be
	 * called one time!
	 * 
	 * @param s
	 */
	public void out(String s) {
		out(new ByteArrayInputStream(s.getBytes()));
	}

	/**
	 * Set inputStream used into {@link Process#getInputStream()}.
	 * 
	 * @param inputStream
	 */
	public void out(InputStream inputStream) {
		this.inputStreamDelegate.setInputStream(inputStream);
	}

	/**
	 * Write string into {@link Process#getErrorStream()}. This method must be
	 * called one time!
	 * 
	 * @param s
	 */
	public void err(String s) {
		err(new ByteArrayInputStream(s.getBytes()));
	}

	/**
	 * Write Exception into {@link Process#getErrorStream()}. This method must
	 * be called one time!
	 * 
	 * @param s
	 */
	public void err(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		pw.close();
		err(sw.getBuffer().toString());
	}

	/**
	 * Set inputStream used into {@link Process#getErrorStream()}.
	 * 
	 * @param inputStream
	 */
	public void err(InputStream inputStream) {
		this.errorStreamDelegate.setInputStream(inputStream);
	}

	// ------------- Thread management

	/**
	 * Start the runnable process into a Thread.
	 */
	protected void start() {
		if (this.state != RunnableState.INITIAL) {
			throw new UnsupportedOperationException(
					"Runnable Process is already started.");
		}
		this.thread.start();
		this.state = RunnableState.RUNNING;
	}

	/**
	 * Create Thread which will use the {@link Runnable} which call the run
	 * method of the process.
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	protected Thread createThread(Runnable runnable, String threadName) {
		Thread t = new Thread(runnable, threadName);
		t.setDaemon(true);
		t.setPriority(Thread.MIN_PRIORITY);
		return t;
	}

	/**
	 * Create {@link Runnable} which call the run method of the process.
	 * 
	 * @return
	 */
	private Runnable createRunnable() {
		return new Runnable() {

			public void run() {
				try {
					// Run process
					RunnableProcess.this.run();
				} catch (Throwable e) {
					// Error process
					err(e);
				} finally {
					destroy();
				}

			}

		};
	}

	private void terminate() {
		// InputStream Delegates must be marked as terminated to
		// kill the
		// thread of OutputStreamMonitor
		// (see explanation at method InputStreamDelegate#returnRead
		RunnableProcess.this.inputStreamDelegate.markAsTerminated();
		RunnableProcess.this.errorStreamDelegate.markAsTerminated();

		synchronized (RunnableProcess.this) {
			RunnableProcess.this.state = RunnableState.TERMINATED;
			RunnableProcess.this.notify();
		}
	}
	
	protected boolean isTerminated() {
		return state == RunnableState.TERMINATED;
	}

	/**
	 * Generate default thread name.
	 * 
	 * @return
	 */
	private synchronized static String generateThreadName() {
		return "Runnable Process-" + threadCount;
	}

	/**
	 * Increment thread count.
	 * 
	 */
	private synchronized static void incrementThreadCount() {
		threadCount++;
	}

	/**
	 * Return thread count.
	 * 
	 * @return
	 */
	public synchronized static int getThreadCount() {
		return threadCount;
	}
}
