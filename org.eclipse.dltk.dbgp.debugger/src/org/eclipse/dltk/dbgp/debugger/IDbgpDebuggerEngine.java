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

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.eclipse.dltk.dbgp.debugger.debugger.event.IDebuggerEventListener;
import org.eclipse.dltk.dbgp.internal.IDbgpTermination;

/**
 * DBGp debugger engine.
 * 
 */
public interface IDbgpDebuggerEngine extends IDbgpTermination,
		IDbgpFeaturesProvider {

	/**
	 * Return the file URI which is debugging.
	 * 
	 * @return
	 */
	URI getFileURI();

	/**
	 * Start the debugger engine.
	 * 
	 * @throws IOException
	 */
	void start() throws IOException;

	/**
	 * Stop the debugger engine.
	 */
	void stop();

	/**
	 * Return true if DBGP protocol (ASCII <->XML) must be traced (into
	 * System.out) and false otherwise.
	 * 
	 * @return
	 */
	boolean isTraceDbgpProtocol();

	/**
	 * Return list of DBGp context supported 'Local', 'Global'... by the engine.
	 * 
	 * @see DBGP specification http://xdebug.org/docs-dbgp.php#context-names
	 * @return
	 */
	Collection<DbgpContext> getDbgpContexts();

}
