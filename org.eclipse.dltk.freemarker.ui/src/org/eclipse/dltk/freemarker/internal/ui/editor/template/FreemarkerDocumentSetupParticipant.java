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
package org.eclipse.dltk.freemarker.internal.ui.editor.template;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerTextTools;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerPartitions;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jface.text.IDocument;

/**
 * The document setup participant for Freemarker.
 */
public class FreemarkerDocumentSetupParticipant implements
		IDocumentSetupParticipant {

	public FreemarkerDocumentSetupParticipant() {

	}

	public void setup(IDocument document) {
		FreemarkerTextTools tools = FreemarkerUIPlugin.getDefault()
				.getTextTools();
		tools.setupDocumentPartitioner(document,
				IFreemarkerPartitions.FREEMARKER_PARTITIONING);
	}
}
