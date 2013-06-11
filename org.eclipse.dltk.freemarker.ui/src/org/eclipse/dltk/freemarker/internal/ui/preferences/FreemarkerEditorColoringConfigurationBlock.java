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
package org.eclipse.dltk.freemarker.internal.ui.preferences;

import java.io.InputStream;

import org.eclipse.dltk.freemarker.internal.ui.FreemarkerPreferenceConstants;
import org.eclipse.dltk.freemarker.internal.ui.editor.template.FreemarkerDocumentSetupParticipant;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerColorConstants;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerPartitions;
import org.eclipse.dltk.freemarker.internal.ui.text.SimpleFreemarkerSourceViewerConfiguration;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.internal.ui.editor.ScriptSourceViewer;
import org.eclipse.dltk.ui.preferences.AbstractScriptEditorColoringConfigurationBlock;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.PreferencesMessages;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.ITextEditor;

public class FreemarkerEditorColoringConfigurationBlock extends
		AbstractScriptEditorColoringConfigurationBlock implements
		IPreferenceConfigurationBlock {

	private static final String PREVIEW_FILE_NAME = "PreviewFile.txt"; //$NON-NLS-1$

	private static final String[][] fSyntaxColorListModel = new String[][] {
			{ PreferencesMessages.DLTKEditorPreferencePage_singleLineComment,
					FreemarkerPreferenceConstants.EDITOR_COMMENT_COLOR,
					sCommentsCategory },

			{ PreferencesMessages.DLTKEditorPreferencePage_keywords,
					FreemarkerPreferenceConstants.EDITOR_KEYWORD_COLOR,
					sCoreCategory },
			{
					FreemarkerPreferencesMessages.FreemarkerEditorPreferencePage_expression,
					FreemarkerPreferenceConstants.EDITOR_EXPRESSION_COLOR,
					sCoreCategory },
			{
					FreemarkerPreferencesMessages.FreemarkerEditorPreferencePage_interpolation,
					FreemarkerPreferenceConstants.EDITOR_INTERPOLATION_COLOR,
					sCoreCategory },

			{ PreferencesMessages.DLTKEditorPreferencePage_strings,
					FreemarkerPreferenceConstants.EDITOR_STRING_COLOR,
					sCoreCategory },
			{ PreferencesMessages.DLTKEditorPreferencePage_default,
					IFreemarkerColorConstants.FREEMARKER_DEFAULT, sCoreCategory },
	// { PreferencesMessages.DLTKEditorPreferencePage_numbers,
	// FreemarkerPreferenceConstants.EDITOR_NUMBER_COLOR,
	// sCoreCategory },
	// { FreemarkerPreferencesMessages.FreemarkerClassVariable,
	// FreemarkerPreferenceConstants.EDITOR_CLASS_VARIABLE_COLOR,
	// sCoreCategory },
	// {
	// FreemarkerPreferencesMessages.FreemarkerInstanceVariable,
	// FreemarkerPreferenceConstants.EDITOR_INSTANCE_VARIABLE_COLOR,
	// sCoreCategory },
	// { FreemarkerPreferencesMessages.FreemarkerGlobalVariable,
	// FreemarkerPreferenceConstants.EDITOR_GLOBAL_VARIABLE_COLOR,
	// sCoreCategory },
	// { FreemarkerPreferencesMessages.FreemarkerPseudoVariable,
	// FreemarkerPreferenceConstants.EDITOR_PSEUDO_VARIABLE_COLOR,
	// sCoreCategory },
	// { FreemarkerPreferencesMessages.FreemarkerSymbols,
	// FreemarkerPreferenceConstants.EDITOR_SYMBOLS_COLOR,
	// sCoreCategory },
	};

	public FreemarkerEditorColoringConfigurationBlock(
			OverlayPreferenceStore store) {
		super(store);
	}

	protected String[][] getSyntaxColorListModel() {
		return fSyntaxColorListModel;
	}

	protected ProjectionViewer createPreviewViewer(Composite parent,
			IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles, IPreferenceStore store) {
		return new ScriptSourceViewer(parent, verticalRuler, overviewRuler,
				showAnnotationsOverview, styles, store);
	}

	protected ScriptSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IColorManager colorManager, IPreferenceStore preferenceStore,
			ITextEditor editor, boolean configureFormatter) {
		return new SimpleFreemarkerSourceViewerConfiguration(colorManager,
				preferenceStore, editor,
				IFreemarkerPartitions.FREEMARKER_PARTITIONING,
				configureFormatter);
	}

	protected void setDocumentPartitioning(IDocument document) {
		FreemarkerDocumentSetupParticipant participant = new FreemarkerDocumentSetupParticipant();
		participant.setup(document);
	}

	protected InputStream getPreviewContentReader() {
		return getClass().getResourceAsStream(PREVIEW_FILE_NAME);
	}

	protected ScriptTextTools getTextTools() {
		return FreemarkerUIPlugin.getDefault().getTextTools();
	}

}
