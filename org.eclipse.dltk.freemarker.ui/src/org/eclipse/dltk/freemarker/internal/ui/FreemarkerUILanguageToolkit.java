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

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.freemarker.core.FreemarkerLanguageToolkit;
import org.eclipse.dltk.freemarker.core.IFreemarkerConstants;
import org.eclipse.dltk.freemarker.internal.ui.editor.template.TemplateEditor;
import org.eclipse.dltk.freemarker.internal.ui.text.SimpleFreemarkerSourceViewerConfiguration;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.viewsupport.ScriptUILabelProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Freemarker DLTK {@link IDLTKUILanguageToolkit} implementation.
 * 
 */
public class FreemarkerUILanguageToolkit extends AbstractDLTKUILanguageToolkit {
	
	private static ScriptElementLabels INSTANCE = new ScriptElementLabels() {
	};

	public ScriptElementLabels getScriptElementLabels() {
		return INSTANCE;
	}

	public IPreferenceStore getPreferenceStore() {
		return FreemarkerUIPlugin.getDefault().getPreferenceStore();
	}

	public IDLTKLanguageToolkit getCoreToolkit() {
		return FreemarkerLanguageToolkit.getDefault();
	}

	public IDialogSettings getDialogSettings() {
		return FreemarkerUIPlugin.getDefault().getDialogSettings();
	}

	public String getEditorId(Object inputElement) {
		return TemplateEditor.FREEMARKER_EDITOR_ID;
	}

	public String getPartitioningId() {
		return IFreemarkerConstants.FREEMARKER_PARTITIONING;
	}

	public String getInterpreterContainerId() {
		return "org.eclipse.dltk.freemarker.launching.INTERPRETER_CONTAINER";
	}

	public ScriptUILabelProvider createScriptUILabelProvider() {
		return null;
	}

	public boolean getProvideMembers(ISourceModule element) {
		return true;
	}

	public ScriptTextTools getTextTools() {
		return FreemarkerUIPlugin.getDefault().getTextTools();
	}

	public ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
		return new SimpleFreemarkerSourceViewerConfiguration(getTextTools()
				.getColorManager(), getPreferenceStore(), null,
				getPartitioningId(), false);
	}

	public String getInterpreterPreferencePage() {
		return "org.eclipse.dltk.debug.ui.FreemarkerInterpreters";
	}

	public String getDebugPreferencePage() {
		return "org.eclipse.dltk.freemarker.preferences.debug";
	}

	private static final String[] EDITOR_PREFERENCE_PAGES_IDS = {
			"org.eclipse.dltk.freemarker.ui.EditorPreferences",
			"org.eclipse.dltk.freemarker.ui.editor.SyntaxColoring",
			"org.eclipse.dltk.freemarker.ui.editor.SmartTyping",
			"org.eclipse.dltk.freemarker.ui.editor.JavascriptFolding",
			"freemarkerTemplatePreferencePage" };

	public String[] getEditorPreferencePages() {
		return EDITOR_PREFERENCE_PAGES_IDS;
	}

}
