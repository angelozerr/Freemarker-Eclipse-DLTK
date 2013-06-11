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
package org.eclipse.dltk.freemarker.internal.ui.text;

import org.eclipse.dltk.ui.text.DLTKColorConstants;

/**
 * Freemarker color constants.
 * 
 */
public interface IFreemarkerColorConstants {

	/**
	 * The color key for string and character literals in Freemarker code.
	 */
	public static final String FREEMARKER_STRING = DLTKColorConstants.DLTK_STRING;

	/**
	 * The color key for Freemarker comments.
	 */
	public static final String FREEMARKER_COMMENT = DLTKColorConstants.DLTK_MULTI_LINE_COMMENT;

	/**
	 * The color key for Freemarker numbers.
	 */
	public static final String FREEMARKER_NUMBER = DLTKColorConstants.DLTK_NUMBER;

	/**
	 * The color key for Freemarker keywords.
	 */
	public static final String FREEMARKER_KEYWORD = DLTKColorConstants.DLTK_KEYWORD;

	/**
	 * The color key for Freemarker interpolation.
	 */
	public static final String FREEMARKER_INTERPOLATION = FREEMARKER_STRING + ".interpolation";

	/**
	 * The color key for Freemarker expression.
	 */
	public static final String FREEMARKER_EXPRESSION = FREEMARKER_STRING + ".expr";

	/**
	 * The color key for Freemarker keyword 'return'.
	 */
	public static final String FREEMARKER_KEYWORD_RETURN = DLTKColorConstants.DLTK_KEYWORD_RETURN;

	/**
	 * The color key for Freemarker code.
	 */
	public static final String FREEMARKER_DEFAULT = DLTKColorConstants.DLTK_DEFAULT;

	/**
	 * The color key for Freemarker doc.
	 */
	public static final String FREEMARKER_DOC = DLTKColorConstants.DLTK_DOC;
}
