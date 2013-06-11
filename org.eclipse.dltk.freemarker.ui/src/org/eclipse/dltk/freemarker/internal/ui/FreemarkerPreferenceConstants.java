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
package org.eclipse.dltk.freemarker.internal.ui;

import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerColorConstants;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * Freemarker DLTK preferences constants.
 *
 */
public class FreemarkerPreferenceConstants extends PreferenceConstants {

	/*
	 * Multiple line comment
	 */
	/**
	 * A named preference that holds the color used to render single line
	 * comments.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_COMMENT_COLOR = IFreemarkerColorConstants.FREEMARKER_COMMENT;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_COMMENT_BOLD = IFreemarkerColorConstants.FREEMARKER_COMMENT
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_COMMENT_ITALIC = IFreemarkerColorConstants.FREEMARKER_COMMENT
			+ EDITOR_ITALIC_SUFFIX;

	/*
	 * Key worlds
	 */
	/**
	 * A named preference that holds the color used to render keyword.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_KEYWORD_COLOR = IFreemarkerColorConstants.FREEMARKER_KEYWORD;

	/**
	 * A named preference that controls whether keyword are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_BOLD = IFreemarkerColorConstants.FREEMARKER_KEYWORD
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_ITALIC = IFreemarkerColorConstants.FREEMARKER_KEYWORD
			+ EDITOR_ITALIC_SUFFIX;

	/*
	 * Freemarker Interpolation
	 */
	/**
	 * A named preference that holds the color used to render interpolation.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_INTERPOLATION_COLOR = IFreemarkerColorConstants.FREEMARKER_INTERPOLATION;

	/**
	 * A named preference that controls whether interpolation are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_INTERPOLATION_BOLD = IFreemarkerColorConstants.FREEMARKER_INTERPOLATION
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether interpolation are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_INTERPOLATION_ITALIC = IFreemarkerColorConstants.FREEMARKER_INTERPOLATION
			+ EDITOR_ITALIC_SUFFIX;

	/*
	 * Freemarker Expression
	 */
	/**
	 * A named preference that holds the color used to render Expression.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_EXPRESSION_COLOR = IFreemarkerColorConstants.FREEMARKER_EXPRESSION;

	/**
	 * A named preference that controls whether Expression are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_EXPRESSION_BOLD = IFreemarkerColorConstants.FREEMARKER_EXPRESSION
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether Expression are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_EXPRESSION_ITALIC = IFreemarkerColorConstants.FREEMARKER_EXPRESSION
			+ EDITOR_ITALIC_SUFFIX;

	/*
	 * Strings
	 */
	/**
	 * A named preference that holds the color used to render STRING.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_STRING_COLOR = IFreemarkerColorConstants.FREEMARKER_STRING;

	public static void initializeDefaultValues(IPreferenceStore store) {
		PreferenceConstants.initializeDefaultValues(store);

		// Editor keyword, .. color, bold, italic
		PreferenceConverter.setDefault(store,
				EDITOR_KEYWORD_COLOR, new RGB(
						127, 0, 85));
		store.setDefault(EDITOR_KEYWORD_BOLD, true);
		store.setDefault(EDITOR_KEYWORD_ITALIC, false);
		
		// Editor comments color, bold
		PreferenceConverter.setDefault(store, EDITOR_COMMENT_COLOR,
				new RGB(63, 127, 95));
		store.setDefault(EDITOR_COMMENT_BOLD, false);
		store.setDefault(EDITOR_COMMENT_ITALIC, true);

		// Editor expression, .. color, bold, italic
		PreferenceConverter.setDefault(store,
				EDITOR_EXPRESSION_COLOR, new RGB(
						0, 0, 192));
		store.setDefault(EDITOR_EXPRESSION_BOLD, false);
		store.setDefault(EDITOR_EXPRESSION_ITALIC, false);

		// Editor interpolation, .. color, bold, italic
		PreferenceConverter.setDefault(store,
				EDITOR_INTERPOLATION_COLOR, new RGB(
						0, 0, 128));
		store.setDefault(EDITOR_INTERPOLATION_BOLD, true);
		store.setDefault(EDITOR_INTERPOLATION_ITALIC, false);

		PreferenceConverter.setDefault(store, EDITOR_STRING_COLOR, new RGB(42,
				0, 255));
		
		store.setDefault(PreferenceConstants.EDITOR_SMART_HOME_END, true);
		store.setDefault(PreferenceConstants.EDITOR_SMART_INDENT, true);
		store.setDefault(PreferenceConstants.EDITOR_SMART_PASTE, true);
		store.setDefault(PreferenceConstants.EDITOR_SMART_TAB, true);
		store.setDefault(EDITOR_SMART_INDENT, true);
		store.setDefault(EDITOR_TAB_ALWAYS_INDENT, true);
		store.setDefault(EDITOR_TAB_WIDTH, 2);

		// Set Formatter preferences (tabulation)
		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_CHAR,
				CodeFormatterConstants.TAB);
		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		store
				.setDefault(CodeFormatterConstants.FORMATTER_INDENTATION_SIZE,
						"4");
		
		// folding
		initializeFoldingDefaults(store);

	}
	
	/**
	 * Initialize Folding defaults
	 * 
	 * @param store
	 */
	protected static void initializeFoldingDefaults(IPreferenceStore store) {
		store.setDefault(PreferenceConstants.EDITOR_FOLDING_ENABLED, true);
		store.setDefault(PreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		store.setDefault(PreferenceConstants.EDITOR_COMMENTS_FOLDING_ENABLED,
				true);
		store.setDefault(PreferenceConstants.EDITOR_DOCS_FOLDING_ENABLED, true);
		store
				.setDefault(PreferenceConstants.EDITOR_FOLDING_INIT_COMMENTS,
						true);
	}
}
