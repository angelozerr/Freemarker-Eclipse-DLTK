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

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.freemarker.internal.ui.text.completion.FreemarkerCompletionProcessor;
import org.eclipse.dltk.freemarker.internal.ui.text.completion.FreemarkerContentAssistPreference;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.FTLTagScannerRegistry;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.FreemarkerCodeScanner;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.FreemarkerStringScanner;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.comment.FreemarkerCommentScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptPresentationReconciler;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.TodoTaskPreferencesOnPreferenceStore;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.eclipse.jface.internal.text.html.HTMLTextPresenter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * Freemarker DLTK {@link ScriptSourceViewerConfiguration} implementation.
 * 
 */
public class FreemarkerSourceViewerConfiguration extends
		ScriptSourceViewerConfiguration {

	private static final String HYPERLINK_CODE_KEY = "org.eclipse.dltk.freemarker.code";
	private FreemarkerTextTools fTextTools;
	private FreemarkerCodeScanner fCodeScanner;
	private FreemarkerCommentScanner fCommentScanner;
	private FreemarkerStringScanner fStringScanner;

	private FTLTagScannerRegistry tagScannerRegistry;

	// TODO : implement tagSyntaxProvider
	private TagSyntaxProvider tagSyntaxProvider;
	
	public FreemarkerSourceViewerConfiguration(IColorManager colorManager,
			IPreferenceStore preferenceStore, ITextEditor editor,
			String partitioning) {
		super(colorManager, preferenceStore, editor, partitioning);
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return IFreemarkerPartitions.FREEMARKER_PARTITION_TYPES;
	}

	@Override
	protected ContentAssistPreference getContentAssistPreference() {
		return FreemarkerContentAssistPreference.getDefault();
	}

	@Override
	protected void initializeScanners() {
		Assert.isTrue(isNewSetup());
		fCodeScanner = new FreemarkerCodeScanner(tagSyntaxProvider, getColorManager(),
				fPreferenceStore);

		// Initialize FTL Tag scanners
		getTagScannerRegistry().initializeScanners(tagSyntaxProvider, getColorManager(),
				fPreferenceStore);

		fCommentScanner = new FreemarkerCommentScanner(getColorManager(),
				fPreferenceStore, IFreemarkerColorConstants.FREEMARKER_COMMENT,
				IFreemarkerColorConstants.FREEMARKER_COMMENT,
				new TodoTaskPreferencesOnPreferenceStore(fPreferenceStore));

		fStringScanner = new FreemarkerStringScanner(getColorManager(),
				fPreferenceStore);
	}

	/**
	 * @return <code>true</code> iff the new setup without text tools is in use.
	 */
	private boolean isNewSetup() {
		return fTextTools == null;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new ScriptPresentationReconciler();
		reconciler
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(fCodeScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(fStringScanner);
		reconciler.setDamager(dr, IFreemarkerPartitions.FREEMARKER_STRING);
		reconciler.setRepairer(dr, IFreemarkerPartitions.FREEMARKER_STRING);

		dr = new DefaultDamagerRepairer(getCommentScanner());
		reconciler.setDamager(dr, IFreemarkerPartitions.FREEMARKER_COMMENT);
		reconciler.setRepairer(dr, IFreemarkerPartitions.FREEMARKER_COMMENT);

		// FTL Tag
		getTagScannerRegistry().setDamagerRepairer(reconciler);

		return reconciler;
	}

	public FreemarkerCommentScanner getCommentScanner() {
		return fCommentScanner;
	}

	/**
	 * Adapts the behavior of the contained components to the change encoded in
	 * the given event.
	 * <p>
	 * Clients are not allowed to call this method if the old setup with text
	 * tools is in use.
	 * </p>
	 * 
	 * @param event
	 *            the event to which to adapt
	 * @see RubySourceViewerConfiguration#ScriptSourceViewerConfiguration(IColorManager,
	 *      IPreferenceStore, ITextEditor, String)
	 */
	@Override
	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		Assert.isTrue(isNewSetup());
		if (fCodeScanner.affectsBehavior(event))
			fCodeScanner.adaptToPreferenceChange(event);
		if (fStringScanner.affectsBehavior(event))
			fStringScanner.adaptToPreferenceChange(event);
		// if (fSingleQuoteStringScanner.affectsBehavior(event))
		// fSingleQuoteStringScanner.adaptToPreferenceChange(event);
		// if (fDocScanner.affectsBehavior(event))
		// fDocScanner.adaptToPreferenceChange(event);
		if (fCommentScanner.affectsBehavior(event))
			fCommentScanner.adaptToPreferenceChange(event);
		// FTL Tags
		getTagScannerRegistry().handlePropertyChangeEvent(event);
	}

	/**
	 * Determines whether the preference change encoded by the given event
	 * changes the behavior of one of its contained components.
	 * 
	 * @param event
	 *            the event to be investigated
	 * @return <code>true</code> if event causes a behavioral change
	 * 
	 */
	@Override
	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		return fCodeScanner.affectsBehavior(event)
				|| fStringScanner.affectsBehavior(event)
				|| fCommentScanner.affectsBehavior(event)
				// FTL Tags
				|| getTagScannerRegistry().affectsTextPresentation(event)
		/*
		 * || fSingleQuoteStringScanner.affectsBehavior(event) ||
		 * fCommentScanner.affectsBehavior(event) ||
		 * fDocScanner.affectsBehavior(event)
		 */;
	}

	@Override
	public IInformationControlCreator getInformationControlCreator(
			ISourceViewer sourceViewer) {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, SWT.NONE,
						new HTMLTextPresenter(true));
			}
		};
	}

	@Override
	protected IInformationControlCreator getOutlinePresenterControlCreator(
			ISourceViewer sourceViewer, final String commandId) {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				int shellStyle = SWT.RESIZE;
				int treeStyle = SWT.V_SCROLL | SWT.H_SCROLL;
				return new FreemarkerOutlineInformationControl(parent,
						shellStyle, treeStyle, commandId);
			}
		};
	}

	// ----------- Code completion

	protected void alterContentAssistant(ContentAssistant assistant) {
		IContentAssistProcessor scriptProcessor = new FreemarkerCompletionProcessor(
				getEditor(), assistant, IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(scriptProcessor,
				IDocument.DEFAULT_CONTENT_TYPE);
		// assistant.setContentAssistProcessor(scriptProcessor,
		// IRubyPartitions.RUBY_SINGLE_QUOTE_STRING);
		assistant.setContentAssistProcessor(scriptProcessor,
				IFreemarkerPartitions.FREEMARKER_STRING);
		// FTL Tags
		tagScannerRegistry.alterContentAssistant(scriptProcessor, assistant);		
	}

	@Override
	protected Map getHyperlinkDetectorTargets(final ISourceViewer sourceViewer) {
		final Map targets = super.getHyperlinkDetectorTargets(sourceViewer);
		targets.put(HYPERLINK_CODE_KEY, getEditor()); //$NON-NLS-1$
		return targets;
	}

	public FTLTagScannerRegistry getTagScannerRegistry() {
		if (tagScannerRegistry == null) {
			tagScannerRegistry = new FTLTagScannerRegistry();
		}
		return tagScannerRegistry;
	}
}
