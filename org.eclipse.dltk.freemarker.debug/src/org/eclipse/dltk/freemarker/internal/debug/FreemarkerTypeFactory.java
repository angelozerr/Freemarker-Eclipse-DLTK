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
package org.eclipse.dltk.freemarker.internal.debug;

import org.eclipse.dltk.debug.core.model.AtomicScriptType;
import org.eclipse.dltk.debug.core.model.ComplexScriptType;
import org.eclipse.dltk.debug.core.model.IScriptType;
import org.eclipse.dltk.debug.core.model.IScriptTypeFactory;

/**
 * 
 * Freemarker {@link IScriptTypeFactory} implementation.
 * 
 * The goal of this DLTK factory is to translate Freemarker language types
 * into Java types that are displayable in for instance variables view when
 * debugging
 * 
 */
public class FreemarkerTypeFactory implements IScriptTypeFactory {

	private static final String[] atomicTypes = {"bool"};

	public FreemarkerTypeFactory() {

	}

	public IScriptType buildType(String type) {
		// TODO : manage buildType
		for (int i = 0; i < atomicTypes.length; ++i) {
			if (atomicTypes[i].equals(type)) {
				return new AtomicScriptType(type);
			}
		}

		return new ComplexScriptType(type);
	}
}