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
package org.eclipse.dltk.freemarker.internal.ui.text.rules.interpolation;

import org.eclipse.dltk.freemarker.internal.ui.text.scanners.AbstractFreemarkerScanner;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class FTLStartInterpolationRule implements IRule {

	private IToken fToken;

	public FTLStartInterpolationRule(IToken token) {
		fToken = token;
	}

	public IToken evaluate(ICharacterScanner scanner) {

		int c = scanner.read();
		if (c == '$' || c == '#') {
			boolean start = ((AbstractFreemarkerScanner) scanner).isStartRead();
			if (start) {
				int charNext = scanner.read();
				if (charNext == '{') {
					return fToken;
				}
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

}
