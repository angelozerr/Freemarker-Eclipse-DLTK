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
package org.eclipse.dltk.internal.launching;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link InputStream} delegate.
 * 
 * This class could interest DLTK project?
 */
public class InputStreamWrapper extends InputStream {

	private InputStream inputStream;
	private boolean terminated = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#available()
	 */
	public int available() throws IOException {
		return inputStream.available();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#close()
	 */
	public void close() throws IOException {
		if (isInitialized())
			inputStream.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!isInitialized())
			return super.equals(obj);
		return inputStream.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (!isInitialized())
			return super.hashCode();
		return inputStream.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#mark(int)
	 */
	public void mark(int readlimit) {
		if (isInitialized())
			inputStream.mark(readlimit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#markSupported()
	 */
	public boolean markSupported() {
		if (!isInitialized())
			return false;
		return inputStream.markSupported();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	public synchronized int read() throws IOException {
		waitInitialized();
		if (!isInitialized())
			return -1;
		return inputStream.read();
	}

	/**
	 * Wait until Inpustream is filled or terminated.
	 */
	private synchronized void waitInitialized() {
		if (!isInitialized() && !terminated) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		waitInitialized();
		if (!isInitialized())
			return -1;
		return inputStream.read(b, off, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read(byte[])
	 */
	public int read(byte[] b) throws IOException {
		waitInitialized();
		if (!isInitialized())
			return -1;
		return inputStream.read(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#reset()
	 */
	public void reset() throws IOException {
		if (isInitialized())
			inputStream.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#skip(long)
	 */
	public long skip(long n) throws IOException {
		if (!isInitialized())
			return 0;
		return inputStream.skip(n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (!isInitialized())
			return super.toString();
		return inputStream.toString();
	}

	/**
	 * Set the inputstream to wrap.
	 * 
	 * @param inputStream
	 */
	public synchronized void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
		notify();
	}

	/**
	 * Return true if there is inputStream to wrap and false otherwise.
	 * 
	 * @return
	 */
	public boolean isInitialized() {
		return (inputStream != null);
	}

	/**
	 * Mark the wrapper as terminated (means that the wrapper will not used
	 * again).
	 */
	public synchronized void markAsTerminated() {
		terminated = true;
		notify();
	}
}
