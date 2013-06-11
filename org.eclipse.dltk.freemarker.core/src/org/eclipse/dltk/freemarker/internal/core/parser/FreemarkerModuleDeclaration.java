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

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

/**
 * Freemarker DLTK {@link ModuleDeclaration} implementation.
 * 
 */
public class FreemarkerModuleDeclaration extends ModuleDeclaration {

	public FreemarkerModuleDeclaration(int sourceLength) {
		super(sourceLength, true);
	}

}
