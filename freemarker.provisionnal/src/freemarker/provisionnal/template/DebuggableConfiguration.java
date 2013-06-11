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
package freemarker.provisionnal.template;

import java.io.IOException;
import java.util.Locale;


import freemarker.provisionnal.debug.impl.DebuggerService;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Debuggable configuration which register the template into a debugger service
 * when it is created.
 * 
 */
public class DebuggableConfiguration extends Configuration {

	private final DebuggerService debuggerService;

	public DebuggableConfiguration(DebuggerService debuggerService) {
		this.debuggerService = debuggerService;
	}

	@Override
	public Template getTemplate(String name, Locale locale, String encoding,
			boolean parse) throws IOException {
		Template template = super.getTemplate(name, locale, encoding, parse);
		if (template != null) {
			// Template is created (ex : with <#import 'test.ftl' > directive),
			// register it to the debugger service.
			debuggerService.registerTemplate(template);
		}
		return template;
	}
}
