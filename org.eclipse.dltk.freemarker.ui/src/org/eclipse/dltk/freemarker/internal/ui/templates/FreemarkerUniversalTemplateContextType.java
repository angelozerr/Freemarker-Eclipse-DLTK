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
package org.eclipse.dltk.freemarker.internal.ui.templates;

import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ui.templates.ScriptTemplateContext;
import org.eclipse.dltk.ui.templates.ScriptTemplateContextType;
import org.eclipse.jface.text.IDocument;

/**
 * 
 * Freemarker (Universal) template context type.
 *
 */
public class FreemarkerUniversalTemplateContextType extends ScriptTemplateContextType {
	public static final String CONTEXT_TYPE_ID = "freemarkerUniversalTemplateContextType"; //$NON-NLS-1$
	
	public FreemarkerUniversalTemplateContextType() {		
		// empty constructor
	}
	
	public FreemarkerUniversalTemplateContextType(String id) {
		super(id);
	}
	
	public FreemarkerUniversalTemplateContextType(String id, String name) {
		super(id, name);
	}
	
	public ScriptTemplateContext createContext(IDocument document, int offset,
			int length, ISourceModule sourceModule) {
		return new FreemarkerTemplateContext(this, document, offset, length, sourceModule);
	}		

}
