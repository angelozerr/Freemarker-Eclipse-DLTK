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
package org.eclipse.dltk.freemarker.internal.core.parser;

import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.core.AbstractSourceElementParser;
import org.eclipse.dltk.core.ISourceElementParser;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.freemarker.internal.core.parser.visitors.FreemarkerSourceElementRequestorVisitor;

/**
 * Freemarker DLTK {@link ISourceElementParser} implementation.
 * 
 */
public class FreemarkerSourceElementParser extends AbstractSourceElementParser {

	protected String getNatureId() {
		return FreemarkerNature.NATURE_ID;
	}

	@Override
	protected SourceElementRequestVisitor createVisitor() {
		return new FreemarkerSourceElementRequestorVisitor(getRequestor());
	}
}
