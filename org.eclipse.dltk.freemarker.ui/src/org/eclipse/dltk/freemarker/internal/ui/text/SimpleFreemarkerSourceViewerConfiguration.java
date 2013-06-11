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
package org.eclipse.dltk.freemarker.internal.ui.text;

import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Simple Freemarker DLTK {@link ScriptSourceViewerConfiguration}
 * implementation.
 * 
 */
public class SimpleFreemarkerSourceViewerConfiguration extends
		FreemarkerSourceViewerConfiguration {

	private boolean fConfigureFormatter;

	public SimpleFreemarkerSourceViewerConfiguration(
			IColorManager colorManager, IPreferenceStore preferenceStore,
			ITextEditor editor, String partitioning, boolean configureFormatter) {
		super(colorManager, preferenceStore, editor, partitioning);
		fConfigureFormatter = configureFormatter;
	}

	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		return null;
	}

	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return null;
	}

	public IAnnotationHover getOverviewRulerAnnotationHover(
			ISourceViewer sourceViewer) {
		return null;
	}

	public int[] getConfiguredTextHoverStateMasks(ISourceViewer sourceViewer,
			String contentType) {
		return null;
	}

	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType, int stateMask) {
		return null;
	}

	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		return null;
	}

	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		if (fConfigureFormatter)
			return super.getContentFormatter(sourceViewer);
		else
			return null;
	}

	public IInformationControlCreator getInformationControlCreator(
			ISourceViewer sourceViewer) {
		return null;
	}

	public IInformationPresenter getInformationPresenter(
			ISourceViewer sourceViewer) {
		return null;
	}

	public IInformationPresenter getOutlinePresenter(
			ISourceViewer sourceViewer, boolean doCodeResolve) {
		return null;
	}

	public IInformationPresenter getHierarchyPresenter(
			ISourceViewer sourceViewer, boolean doCodeResolve) {
		return null;
	}

	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		return null;
	}

}
