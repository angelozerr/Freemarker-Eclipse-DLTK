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

/**
 * DBGp context supported by a DBGP debugger engine.
 * 
 * @see DBGP specification http://xdebug.org/docs-dbgp.php#context-names
 */
public class DbgpContext {

	// Default DBGp context name
	public static final String DBGP_CONTEXT_LOCAL_NAME = "Local";
	public static final String DBGP_CONTEXT_GLOBAL_NAME = "Global";
	public static final String DBGP_CONTEXT_CLASS_NAME = "Class";

	// Default DBGp context id
	public static final int DBGP_CONTEXT_LOCAL_ID = 0;
	public static final int DBGP_CONTEXT_GLOBAL_ID = 1;
	public static final int DBGP_CONTEXT_CLASS_ID = 2;

	public static final DbgpContext DBGP_CONTEXT_LOCAL = new DbgpContext(
			DBGP_CONTEXT_LOCAL_ID, DBGP_CONTEXT_LOCAL_NAME);
	public static final DbgpContext DBGP_CONTEXT_GLOBAL = new DbgpContext(
			DBGP_CONTEXT_GLOBAL_ID, DBGP_CONTEXT_GLOBAL_NAME);
	public static final DbgpContext DBGP_CONTEXT_CLASS = new DbgpContext(
			DBGP_CONTEXT_CLASS_ID, DBGP_CONTEXT_CLASS_NAME);

	private final int id;
	private final String name;

	public DbgpContext(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Return the id of the DBGP context (ex : 0, 1, 2..).
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Return the name of the DBGP context (ex : Local, Global, Class).
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
}
