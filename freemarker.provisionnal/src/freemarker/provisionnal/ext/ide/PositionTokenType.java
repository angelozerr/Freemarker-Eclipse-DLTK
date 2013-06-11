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
package freemarker.provisionnal.ext.ide;

/**
 * Position token type.
 * 
 */
public enum PositionTokenType {

	UNKNOWN(""), EXPRESSION(""), INTERPOLATION("{"), IF_DIRECTIVE("if "), LIST_DIRECTIVE(
			"list ");

	private String id;

	private PositionTokenType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
