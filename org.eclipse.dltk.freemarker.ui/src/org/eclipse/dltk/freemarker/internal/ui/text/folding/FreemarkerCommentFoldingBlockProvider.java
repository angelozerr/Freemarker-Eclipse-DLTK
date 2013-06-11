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
package org.eclipse.dltk.freemarker.internal.ui.text.folding;

import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerPartitioningProvider;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerPartitions;
import org.eclipse.dltk.ui.text.folding.IFoldingContent;
import org.eclipse.dltk.ui.text.folding.PartitioningFoldingBlockProvider;

/**
 * Manage folding (expand/collapse Freemarker comments) with the Freemarker
 * editor.
 * 
 */
public class FreemarkerCommentFoldingBlockProvider extends
		PartitioningFoldingBlockProvider {

	public FreemarkerCommentFoldingBlockProvider() {
		super(FreemarkerPartitioningProvider.getInstance());
	}

	public void computeFoldableBlocks(IFoldingContent content) {
		if (isFoldingComments())
			computeBlocksForPartitionType(content,
					IFreemarkerPartitions.FREEMARKER_COMMENT,
					FreemarkerFoldingBlockKind.COMMENT, isCollapseComments());
		// if (isFoldingDocs())
		// computeBlocksForPartitionType(content,
		// IFreemarkerPartitions.JS_DOC,
		// FreemarkerFoldingBlockKind.JSDOC, isCollapseDocs());
	}

}
