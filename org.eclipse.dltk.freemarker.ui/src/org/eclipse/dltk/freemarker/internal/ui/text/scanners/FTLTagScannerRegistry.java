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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerPartitions;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.directives.FreemarkerAssignTagScanner;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.directives.FreemarkerIfTagScanner;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.directives.FreemarkerIncludeTagScanner;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.directives.FreemarkerListTagScanner;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.interpolation.FreemarkerInterpolationScanner;
import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.util.PropertyChangeEvent;

import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

/**
 * Registry for FTL Tag Scanner.
 * 
 */
public class FTLTagScannerRegistry {

	private List<ScannerInfo> scanners = new ArrayList<ScannerInfo>();

	public static class ScannerInfo {

		public final String partitionType;
		public final AbstractScriptScanner scanner;
		public final boolean completionAvailable;
		
		public ScannerInfo(String partitionType, AbstractScriptScanner scanner, boolean completionAvailable) {
			this.partitionType = partitionType;
			this.scanner = scanner;
			this.completionAvailable  = completionAvailable;		}
	}

	public void initializeScanners(TagSyntaxProvider tagSyntaxProvider,
			IColorManager colorManager, IPreferenceStore preferenceStore) {
		// interpolation scanner
		addScanner(IFreemarkerPartitions.FTL_INTERPOLATION,
				new FreemarkerInterpolationScanner(colorManager,
						preferenceStore), true);
		// assign scanner
		addScanner(IFreemarkerPartitions.FTL_ASSIGN,
				new FreemarkerAssignTagScanner(tagSyntaxProvider, colorManager,
						preferenceStore), true);
		// include scanner
		addScanner(IFreemarkerPartitions.FTL_INCLUDE,
				new FreemarkerIncludeTagScanner(tagSyntaxProvider,
						colorManager, preferenceStore), true);
		// if scanner
		addScanner(IFreemarkerPartitions.FTL_IF_DIRECTIVE_START,
				new FreemarkerIfTagScanner(tagSyntaxProvider, colorManager,
						preferenceStore), true);
		// list scanner
		addScanner(IFreemarkerPartitions.FTL_LIST_DIRECTIVE_START,
				new FreemarkerListTagScanner(tagSyntaxProvider, colorManager,
						preferenceStore), true);
	}

	protected void addScanner(String partitionType,
			AbstractScriptScanner scanner, boolean completionAvailable) {
		scanners.add(new ScannerInfo(partitionType, scanner, completionAvailable));
	}

	public void setDamagerRepairer(PresentationReconciler reconciler) {

		for (ScannerInfo info : scanners) {
			DefaultDamagerRepairer dr = new DefaultDamagerRepairer(info.scanner);
			reconciler.setDamager(dr, info.partitionType);
			reconciler.setRepairer(dr, info.partitionType);
		}
	}

	public void alterContentAssistant(IContentAssistProcessor scriptProcessor,
			ContentAssistant assistant) {
		for (ScannerInfo info : scanners) {
			if (info.completionAvailable) {
				String partitionType = info.partitionType;	
				assistant.setContentAssistProcessor(scriptProcessor,
						partitionType);
			}			
		}
	}

	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		for (ScannerInfo info : scanners) {
			AbstractScriptScanner scanner = info.scanner;
			if (scanner.affectsBehavior(event))
				scanner.adaptToPreferenceChange(event);
		}
	}

	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		for (ScannerInfo info : scanners) {
			AbstractScriptScanner scanner = info.scanner;
			if (scanner.affectsBehavior(event)) {
				return true;
			}
		}
		return false;
	}
}
