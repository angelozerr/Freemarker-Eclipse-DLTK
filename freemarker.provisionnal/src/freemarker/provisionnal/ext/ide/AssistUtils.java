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

import freemarker.template.Configuration;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateScalarModel;
import freemarker.template.utility.StringUtil;

/**
 * Utilities to help IDE Code Assist.
 * 
 */
public class AssistUtils {

	// Default value for max length of character to parse.
	private final static int DEFAULT_CHARACTER_PARSED_MAX = 100;

	private static PositionTokenType[] DIRECTIVES_WITH_EXPRESSIONS = {
			PositionTokenType.IF_DIRECTIVE, PositionTokenType.LIST_DIRECTIVE };

	private static String[] EMPTY_STRING_ARRAY = new String[0];

	private static String[] EXPRESSION_OPERATORS = { "+", "-", "/",  "*", "=",
			">=", ">", "<=", "<" };

	/**
	 * Return the {@link PositionCalculator} of the position content and null if
	 * no token are founded.
	 * 
	 * @param content
	 * @param position
	 * @param tagSyntax
	 * @param maxLength
	 * @return
	 */
	public static PositionCalculator getPositionCalculator(String content,
			int position, int tagSyntax) {
		return getPositionCalculator(content, position, tagSyntax,
				DEFAULT_CHARACTER_PARSED_MAX);
	}

	/**
	 * Return the {@link PositionCalculator} of the position content and null if
	 * no token are founded.
	 * 
	 * @param content
	 * @param position
	 * @param tagSyntax
	 * @param maxLength
	 * @return
	 */
	public static PositionCalculator getPositionCalculator(String content,
			int position, int tagSyntax, int maxLength) {
		int length = content.length();
		if (position > length)
			return null;

		int charCount = 0;
		for (int i = position - 1; i >= 0; i--) {
			char c = content.charAt(i);
			switch (c) {
			case ' ':
			case '\r':
			case '\n':
				// Ignore character
				break;
			case '{':
				// Is FM Interpolation Start?
				if (i > 0) {
					c = content.charAt(i - 1);
					if (c == '$') {
						// It's FM Interpolation Start. ('${' string founded)
						// => Get the expression content.

						// Return string between '${' and
						// the cursor position to get the completion content.
						// ex : '${aaa' will return 'aaa' as completion.
						String completion = extractCompletion(content
								.substring(i + 1, position));
						return new PositionCalculator(completion,
								PositionTokenType.EXPRESSION,
								PositionTokenType.INTERPOLATION);
					}
				}
				break;
			case '#':
				// Is FM Directive Start?
				if (i > 0) {
					c = content.charAt(i - 1);
					if (isStartBracket(c, tagSyntax)) {
						// It's FM Directive Start.

						// Search if the directive support expression?
						String s = content.substring(i + 1, position);
						for (int j = 0; j < DIRECTIVES_WITH_EXPRESSIONS.length; j++) {
							PositionTokenType directive = DIRECTIVES_WITH_EXPRESSIONS[j];
							if (s.startsWith(directive.getId())) {
								String completion = extractCompletion(s
										.substring(directive.getId().length(),
												s.length()));
								return new PositionCalculator(completion,
										PositionTokenType.EXPRESSION, directive);
							}
						}

					}
					return null;
				}
				break;
			case '.':
				charCount = 0;
				break;
			default:
				if (charCount > maxLength)
					return null;
				charCount++;
			}
		}

		return null;
	}

	/**
	 * Expression can contain operator string like '+', '-'... (Ex :
	 * ${user+user2}. The completion must be extracted to get the keyword after
	 * the operator (ex : user2).
	 * 
	 * @param completion
	 * @return
	 */
	private static String extractCompletion(String completion) {
		if (completion == null) {
			return null;
		}

		for (int i = 0; i < EXPRESSION_OPERATORS.length; i++) {
			String operator = EXPRESSION_OPERATORS[i];
			int index = completion.lastIndexOf(operator);
			if (index != -1) {
				completion = completion.substring(index + operator.length(),
						completion.length());
				break;
			}
		}
		return completion.trim();
	}

	/**
	 * Get paths from the expression (split expression with '.').
	 * 
	 * @param expression
	 * @return
	 */
	public static String[] getExpressionPaths(String expression) {
		if (expression == null) {
			return EMPTY_STRING_ARRAY;
		}
		String[] paths = expression.split("[.]");
		if (expression.endsWith(".")) {
			String[] newPaths = new String[paths.length + 1];
			System.arraycopy(paths, 0, newPaths, 0, paths.length);
			paths = newPaths;
			paths[paths.length - 1] = "";
		}
		return paths;
	}

	/**
	 * Return true if character is start bracked and false otherwise according
	 * to tagSyntax.
	 * 
	 * @param c
	 * @param tagSyntax
	 * @return
	 */
	private static boolean isStartBracket(char c, int tagSyntax) {
		switch (tagSyntax) {
		case Configuration.AUTO_DETECT_TAG_SYNTAX:
			return (c == '<' || c == '[');
		case Configuration.SQUARE_BRACKET_TAG_SYNTAX:
			return (c == '<');
		case Configuration.ANGLE_BRACKET_TAG_SYNTAX:
			return (c == '[');
		}
		return false;
	}

	/**
	 * Return the string value of {@link TemplateModel}.
	 * 
	 * @param value
	 * @return
	 */
	public static String getValue(TemplateModel value) {
		if (value == null) {
			return null;
		}
		if (value instanceof TemplateScalarModel) {
			StringBuilder result = new StringBuilder();
			result.append("\"");
			result.append(StringUtil.FTLStringLiteralEnc(value.toString()));
			result.append("\"");
			return result.toString();
		}
		return value.toString();
	}
}
