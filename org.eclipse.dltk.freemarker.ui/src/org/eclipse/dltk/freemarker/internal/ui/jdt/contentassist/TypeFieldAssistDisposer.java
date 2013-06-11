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
package org.eclipse.dltk.freemarker.internal.ui.jdt.contentassist;

import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

/**
 * TypeFieldAssistDisposer
 *
 */
public class TypeFieldAssistDisposer {

	private ContentAssistCommandAdapter fAdapter;

	private TypeContentProposalListener fListener;

	/**
	 * 
	 */
	public TypeFieldAssistDisposer(ContentAssistCommandAdapter adapter, TypeContentProposalListener listener) {
		fAdapter = adapter;
		fListener = listener;
	}

	/**
	 * 
	 */
	public void dispose() {
		if (fAdapter == null) {
			return;
		}
		// Dispose of the label provider
		ILabelProvider labelProvider = fAdapter.getLabelProvider();
		if ((labelProvider != null)) {
			fAdapter.setLabelProvider(null);
			labelProvider.dispose();
		}
		// Remove the listeners
		if (fListener != null) {
			fAdapter.removeContentProposalListener((IContentProposalListener) fListener);
			fAdapter.removeContentProposalListener((IContentProposalListener2) fListener);
		}
	}

}
