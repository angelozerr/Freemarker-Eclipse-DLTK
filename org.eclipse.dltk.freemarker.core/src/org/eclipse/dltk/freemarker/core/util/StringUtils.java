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
package org.eclipse.dltk.freemarker.core.util;

/**
 * String Utilities.
 */
public class StringUtils {

	public static String[] EMPTY_STRING_ARRAY = new String[0];
	
	public static boolean isEmpty(String value) {
		return !(value != null && value.length() > 0);
	}

	public static Long getLong(String value) {
		if (isEmpty(value))
			return null;
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
