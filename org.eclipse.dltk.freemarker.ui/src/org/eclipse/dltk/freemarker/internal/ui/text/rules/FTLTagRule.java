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
package org.eclipse.dltk.freemarker.internal.ui.text.rules;

import org.eclipse.jface.text.rules.IToken;

import freemarker.provisionnal.ext.ide.syntax.FTLSyntaxUtils;
import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * Standard implementation of <code>IPredicateRule</code>. Is is capable of
 * detecting a pattern which begins with a given start sequence and ends with a
 * given end sequence. If the end sequence is not specified, it can be either
 * end of line, end or file, or both. Additionally, the pattern can be
 * constrained to begin in a certain column. The rule can also be used to check
 * whether the text to scan covers half of the pattern, i.e. contains the end
 * sequence required by the rule.
 */
public class FTLTagRule extends ChoicePatternRule {

	private TagSyntaxProvider tagSyntaxProvider;

	private int startChar;
	
	/**
	 * Creates a rule for the given starting and ending sequence. When these
	 * sequences are detected the rule will return the specified token.
	 * Alternatively, the sequence can also be ended by the end of the line. Any
	 * character which follows the given escapeCharacter will be ignored.
	 * 
	 * @param startSequence
	 *            the pattern's start sequence
	 * @param endSequence
	 *            the pattern's end sequence, <code>null</code> is a legal value
	 * @param token
	 *            the token which will be returned on success
	 * @param escapeCharacter
	 *            any character following this one will be ignored
	 * @param breaksOnEOL
	 *            indicates whether the end of the line also terminates the
	 *            pattern
	 */
	public FTLTagRule(String tagName,
			TagSyntaxProvider tagSyntaxProvider, IToken token,
			char escapeCharacter, boolean breaksOnEOL) {
		super(tagName, token, escapeCharacter, breaksOnEOL);
		this.tagSyntaxProvider = tagSyntaxProvider;
	}

	/**
	 * Creates a rule for the given starting and ending sequence. When these
	 * sequences are detected the rule will return the specified token.
	 * Alternatively, the sequence can also be ended by the end of the line or
	 * the end of the file. Any character which follows the given
	 * escapeCharacter will be ignored.
	 * 
	 * @param startSequence
	 *            the pattern's start sequence
	 * @param endSequence
	 *            the pattern's end sequence, <code>null</code> is a legal value
	 * @param token
	 *            the token which will be returned on success
	 * @param escapeCharacter
	 *            any character following this one will be ignored
	 * @param breaksOnEOL
	 *            indicates whether the end of the line also terminates the
	 *            pattern
	 * @param breaksOnEOF
	 *            indicates whether the end of the file also terminates the
	 *            pattern
	 * @since 2.1
	 */
	public FTLTagRule(String tagName,
			TagSyntaxProvider tagSyntaxProvider, IToken token,
			char escapeCharacter, boolean breaksOnEOL, boolean breaksOnEOF) {
		super(tagName, token, escapeCharacter, breaksOnEOL, breaksOnEOF);
		this.tagSyntaxProvider = tagSyntaxProvider;
	}

	/**
	 * Creates a rule for the given starting and ending sequence. When these
	 * sequences are detected the rule will return the specified token.
	 * Alternatively, the sequence can also be ended by the end of the line or
	 * the end of the file. Any character which follows the given
	 * escapeCharacter will be ignored. An end of line immediately after the
	 * given <code>lineContinuationCharacter</code> will not cause the pattern
	 * to terminate even if <code>breakOnEOL</code> is set to true.
	 * 
	 * @param startSequence
	 *            the pattern's start sequence
	 * @param endSequence
	 *            the pattern's end sequence, <code>null</code> is a legal value
	 * @param token
	 *            the token which will be returned on success
	 * @param escapeCharacter
	 *            any character following this one will be ignored
	 * @param breaksOnEOL
	 *            indicates whether the end of the line also terminates the
	 *            pattern
	 * @param breaksOnEOF
	 *            indicates whether the end of the file also terminates the
	 *            pattern
	 * @param escapeContinuesLine
	 *            indicates whether the specified escape character is used for
	 *            line continuation, so that an end of line immediately after
	 *            the escape character does not terminate the pattern, even if
	 *            <code>breakOnEOL</code> is set
	 * @since 3.0
	 */
	public FTLTagRule(String tagName,
			TagSyntaxProvider tagSyntaxProvider, IToken token,
			char escapeCharacter, boolean breaksOnEOL, boolean breaksOnEOF,
			boolean escapeContinuesLine) {
		super(tagName, token, escapeCharacter, breaksOnEOL, breaksOnEOF,
				escapeContinuesLine);
		this.tagSyntaxProvider = tagSyntaxProvider;
	}

	@Override
	protected boolean isStartSequence(int c) {
		boolean result = FTLSyntaxUtils.isStartBracket(c, tagSyntaxProvider);
		if (result) {
			startChar = c;
		}
		else {
			startChar = -1;
		}
		return result;
	}

	@Override
	protected boolean isEndSequence(int c) {
		return FTLSyntaxUtils.isEndBracket(c, startChar);
	}

}
