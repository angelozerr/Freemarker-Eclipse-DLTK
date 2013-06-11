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
package freemarker.provisionnal.ext.ide.syntax;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Utilities for {@link Configuration} syntax used into a {@link Template}.
 * 
 */
public class FTLSyntaxUtils {

	/**
	 * Return true if the character is a FTL start bracket according the tag
	 * syntax provider and false otherwise.
	 * 
	 * @param c
	 * @param tagSyntaxProvider
	 * @return
	 */
	public static boolean isStartBracket(int c,
			TagSyntaxProvider tagSyntaxProvider) {
		if (tagSyntaxProvider == null) {
			return c == '<' || c == '[';
		}
		if (tagSyntaxProvider.getTagSyntax() == Configuration.ANGLE_BRACKET_TAG_SYNTAX) {
			return c == '[';
		}
		return c == '<';
	}

	/**
	 * Return true if the character is a FTL end bracket according the tag
	 * syntax provider and false otherwise.
	 * 
	 * @param c
	 * @param tagSyntaxProvider
	 * @return
	 */
	public static boolean isEndBracket(int c,
			TagSyntaxProvider tagSyntaxProvider) {
		if (tagSyntaxProvider == null) {
			return c == '>' || c == ']';
		}
		if (tagSyntaxProvider.getTagSyntax() == Configuration.ANGLE_BRACKET_TAG_SYNTAX) {
			return c == ']';
		}
		return c == '>';
	}

	/**
	 * Return true if the character is a FTL end bracket according the start
	 * bracket and false otherwise.
	 * 
	 * @param c
	 * @param startBracket
	 * @return
	 */
	public static boolean isEndBracket(int c, int startBracket) {
		switch (startBracket) {
		case '<':
			return c == '>';
		case '[':
			return c == ']';
		}
		return false;
	}

}
