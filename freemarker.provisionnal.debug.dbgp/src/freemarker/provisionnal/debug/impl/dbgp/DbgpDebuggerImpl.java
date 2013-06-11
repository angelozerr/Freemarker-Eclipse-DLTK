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
package freemarker.provisionnal.debug.impl.dbgp;

import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.dbgp.debugger.AbstractDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.DbgpContext;
import org.eclipse.dltk.dbgp.debugger.debugger.IDebugger;

import freemarker.provisionnal.template.ConfigurationProvider;
import freemarker.provisionnal.template.ModelProvider;

/**
 * 
 * Freemarker DBGp debugger implementation.
 * 
 */
public class DbgpDebuggerImpl extends AbstractDbgpDebuggerEngine {

	private static final String FM_APPID = "fm_debugger";
	private static final String FM_LANGUAGE = "freemarker";
	private static final String FM_PROTOCOL_VERSION = "1.0";

	private static final Map<String, String> SUPPORTED_FEATURES = new HashMap<String, String>();

	static {
		SUPPORTED_FEATURES.put("language_supports_threads", "0");
		SUPPORTED_FEATURES.put("language_name", "freemarker");
		SUPPORTED_FEATURES.put("language_version", "1.1");
		SUPPORTED_FEATURES.put("encoding", "UTF-8");
		SUPPORTED_FEATURES.put("protocol_version", "1");
		SUPPORTED_FEATURES.put("supports_async", "1");
		SUPPORTED_FEATURES.put("breakpoint_types", "line");
		SUPPORTED_FEATURES.put("multiple_sessions", "0");
		SUPPORTED_FEATURES.put("max_children", "32");
		SUPPORTED_FEATURES.put("max_data", "1024");
		SUPPORTED_FEATURES.put("max_depth", "1");
	}

	private ConfigurationProvider configurationProvider;
	private ModelProvider modelProvider;

	public DbgpDebuggerImpl(InetAddress ideAdress, int port, String ideKey,
			URI fileURI, ConfigurationProvider configurationProvider,
			ModelProvider modelProvider, boolean debugDbgpProtocol) {
		super(ideAdress, port, ideKey, fileURI, debugDbgpProtocol);
		this.configurationProvider = configurationProvider;
		this.modelProvider = modelProvider;
	}

	@Override
	protected IDebugger createDebugger() {
		DbgpDebuggerService debuggerService = new DbgpDebuggerService();
		return new LocalDebuggerImpl(this, debuggerService,
				configurationProvider, modelProvider);
	}

	@Override
	protected String getInitPacketAppid() {
		return FM_APPID;
	}

	@Override
	protected String getInitPacketLanguage() {
		return FM_LANGUAGE;
	}

	@Override
	protected String getInitPacketProtocolVersion() {
		return FM_PROTOCOL_VERSION;
	}

	public String getDbgpFeature(String featureName) {
		return SUPPORTED_FEATURES.get(featureName);
	}

	@Override
	public Collection<DbgpContext> createDbgpContexts() {
		Collection<DbgpContext> contexts = new ArrayList<DbgpContext>();
		contexts.add(DbgpContext.DBGP_CONTEXT_LOCAL);
		contexts.add(DbgpContext.DBGP_CONTEXT_GLOBAL);
		contexts.add(DbgpContext.DBGP_CONTEXT_CLASS);
		return contexts;
	}

}
