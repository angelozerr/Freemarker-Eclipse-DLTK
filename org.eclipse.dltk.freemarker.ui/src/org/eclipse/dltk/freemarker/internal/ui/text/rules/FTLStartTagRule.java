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

import org.eclipse.dltk.freemarker.internal.ui.text.scanners.AbstractFreemarkerScanner;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import freemarker.provisionnal.ext.ide.syntax.FTLSyntaxUtils;
import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

public class FTLStartTagRule implements IRule {

	private TagSyntaxProvider tagSyntaxProvider;
	private IToken fToken;
	/** The pattern's start sequence */
	protected char[] fStartSequence;

	public FTLStartTagRule(String tagName, TagSyntaxProvider tagSyntaxProvider,
			IToken token) {
		fToken = token;
		this.tagSyntaxProvider = tagSyntaxProvider;
		fStartSequence = tagName.toCharArray();
	}

	public IToken evaluate(ICharacterScanner scanner) {

		int c = scanner.read();
		if (FTLSyntaxUtils.isStartBracket(c, tagSyntaxProvider)) {
			AbstractFreemarkerScanner freemarkerScanner = ((AbstractFreemarkerScanner)scanner);
			boolean start = freemarkerScanner
					.isStartRead();
			if (start && sequenceDetected(scanner, fStartSequence, false)) {				
				return fToken;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	/**
	 * Returns whether the next characters to be read by the character scanner
	 * are an exact match with the given sequence. No escape characters are
	 * allowed within the sequence. If specified the sequence is considered to
	 * be found when reading the EOF character.
	 * 
	 * @param scanner
	 *            the character scanner to be used
	 * @param sequence
	 *            the sequence to be detected
	 * @param eofAllowed
	 *            indicated whether EOF terminates the pattern
	 * @return <code>true</code> if the given sequence has been detected
	 */
	protected boolean sequenceDetected(ICharacterScanner scanner,
			char[] sequence, boolean eofAllowed) {
		for (int i = 1; i < sequence.length; i++) {
			int c = scanner.read();
			if (c == ICharacterScanner.EOF && eofAllowed) {
				return true;
			} else if (c != sequence[i]) {
				// Non-matching character detected, rewind the scanner back to
				// the start.
				// Do not unread the first character.
				scanner.unread();
				for (int j = i - 1; j > 0; j--)
					scanner.unread();
				return false;
			}
		}

		return true;
	}
}
