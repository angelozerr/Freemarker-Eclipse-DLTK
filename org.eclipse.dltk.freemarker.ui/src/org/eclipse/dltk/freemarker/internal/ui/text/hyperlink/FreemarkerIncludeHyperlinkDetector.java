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
package org.eclipse.dltk.freemarker.internal.ui.text.hyperlink;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;

public class FreemarkerIncludeHyperlinkDetector extends AbstractHyperlinkDetector {

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion inputRegion, boolean canShowMultipleHyperlinks) {
		if (inputRegion == null || textViewer == null) {
			return null;
		}
		try {
			final IDocument doc = textViewer.getDocument();
			final int lineNumber = doc.getLineOfOffset(inputRegion.getOffset());
			final IRegion region = doc.getLineInformation(lineNumber);
			final String line = doc.get(region.getOffset(), region.getLength());
			if (line != null && line.length() != 0) {
				final IHyperlink link = checkLine(region.getOffset(), line);
				if (link != null) {
					return new IHyperlink[] { link };
				}
			}
		} catch (BadLocationException e) {
//			if (RubyPlugin.DUMP_EXCEPTIONS_TO_CONSOLE) {
//				e.printStackTrace();
//			}
		}
		return null;
	}

	/**
	 * Checks if the specified line matches the <code>require</code> statement
	 * 
	 * @param offset
	 * @param line
	 * @return
	 */
	public IHyperlink checkLine(int offset, final String line) {
		int begin = 0;
		int end = line.length();
		while (begin < end && Character.isWhitespace(line.charAt(begin))) {
			++begin;
		}
		while (begin < end && Character.isWhitespace(line.charAt(end - 1))) {
			--end;
		}
		if (begin + "<#include".length() < end
				&& line.startsWith("<#include", begin)) {
			begin += "<#include".length();
			while (begin < end && Character.isWhitespace(line.charAt(begin))) {
				++begin;
			}
			if (begin + 2 < end && line.charAt(begin) == '('
					&& line.charAt(end - 1) == ')') {
				++begin;
				--end;
				while (begin < end
						&& Character.isWhitespace(line.charAt(begin))) {
					++begin;
				}
				while (begin < end
						&& Character.isWhitespace(line.charAt(end - 1))) {
					--end;
				}
			}
			if (begin + 2 < end) {
				final char quote = line.charAt(begin);
				if ((quote == '\'' || quote == '"')
						&& line.charAt(end - 1) == '>') {
					++begin;
					--end;
					return createLink(offset, line, begin, end);
				}
			}
		}
		return null;
	}

	/**
	 * Creates {@link IHyperlink} instance.
	 * 
	 * This method is extracted to simplify testing.
	 * 
	 * @param offset
	 * @param line
	 * @param begin
	 * @param end
	 * @return
	 */
	protected IHyperlink createLink(int offset, final String line, int begin,
			int end) {
		final ITextEditor editor = (ITextEditor) getAdapter(ITextEditor.class);
		if (editor != null) {
			final String requiredFile = line.substring(begin, end);
			final Region region = new Region(offset + begin, end - begin);
			return new FreemarkerIncludeHyperlink(requiredFile, region, editor);
		}
		return null;
	}

}
