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
package org.eclipse.dltk.freemarker.internal.ui.templates;

import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerTextTools;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerPartitions;
import org.eclipse.dltk.freemarker.internal.ui.text.SimpleFreemarkerSourceViewerConfiguration;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.dltk.ui.templates.ScriptTemplatePreferencePage;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.text.IDocument;

/**
 * Freemarker code templates preference page that you can access at
 * Window/Preferences->Freemarker/Templates.
 */
public class FreemarkerCodeTemplatesPreferencePage extends
		ScriptTemplatePreferencePage {
	/*
	 * @seeorg.eclipse.dltk.ui.templates.ScriptTemplatePreferencePage#
	 * createSourceViewerConfiguration()
	 */
	protected ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
		return new SimpleFreemarkerSourceViewerConfiguration(getTextTools()
				.getColorManager(), getPreferenceStore(), null,
				IFreemarkerPartitions.FREEMARKER_PARTITIONING, false);
	}

	/*
	 * @see
	 * org.eclipse.dltk.ui.templates.ScriptTemplatePreferencePage#getTemplateAccess
	 * ()
	 */
	protected ScriptTemplateAccess getTemplateAccess() {
		return FreemarkerTemplateAccess.getInstance();
	}

	/*
	 * @seeorg.eclipse.dltk.ui.templates.ScriptTemplatePreferencePage#
	 * setDocumentParticioner(org.eclipse.jface.text.IDocument)
	 */
	protected void setDocumentPartitioner(IDocument document) {
		getTextTools().setupDocumentPartitioner(document,
				IFreemarkerPartitions.FREEMARKER_PARTITIONING);
	}

	/*
	 * @see
	 * org.eclipse.dltk.ui.templates.ScriptTemplatePreferencePage#setPreferenceStore
	 * ()
	 */
	protected void setPreferenceStore() {
		setPreferenceStore(FreemarkerUIPlugin.getDefault().getPreferenceStore());
	}

	private FreemarkerTextTools getTextTools() {
		return FreemarkerUIPlugin.getDefault().getTextTools();
	}

}
