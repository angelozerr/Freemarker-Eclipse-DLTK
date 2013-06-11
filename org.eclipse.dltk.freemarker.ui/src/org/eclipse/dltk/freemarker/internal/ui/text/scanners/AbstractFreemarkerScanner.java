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
package org.eclipse.dltk.freemarker.internal.ui.text.scanners;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

public abstract class AbstractFreemarkerScanner extends AbstractScriptScanner {

	private int fStart;
	protected final TagSyntaxProvider tagSyntaxProvider;

	public AbstractFreemarkerScanner(TagSyntaxProvider tagSyntaxProvider,
			IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		this.tagSyntaxProvider = tagSyntaxProvider;
	}

	@Override
	public void setRange(IDocument document, int offset, int length) {
		super.setRange(document, offset, length);
		fStart = offset;
	}

	public boolean isStartRead() {
		return fStart == fTokenOffset;
	}

}
