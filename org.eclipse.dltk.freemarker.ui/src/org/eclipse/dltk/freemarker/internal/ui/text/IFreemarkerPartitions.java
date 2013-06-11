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

import org.eclipse.dltk.freemarker.core.IFreemarkerConstants;
import org.eclipse.jface.text.IDocument;

/**
 * Constants for Freemarker partitions.
 * 
 */
public interface IFreemarkerPartitions {

	public final static String FREEMARKER_PARTITIONING = IFreemarkerConstants.FREEMARKER_PARTITIONING;

	public final static String FREEMARKER_COMMENT = "__freemarker_comment"; //$NON-NLS-1$
	public final static String FREEMARKER_STRING = "____freemarker_string"; //$NON-NLS-1$

	public final static String FTL_ASSIGN = "__ftl_assign"; //$NON-NLS-1$
	public final static String FTL_INCLUDE = "__ftl_include"; //$NON-NLS-1$
	public final static String FTL_IF_DIRECTIVE_START = "__ftl_if_directive_start"; //$NON-NLS-1$
	public final static String FTL_LIST_DIRECTIVE_START = "__ftl_list_directive_start"; //$NON-NLS-1$

	public final static String FTL_INTERPOLATION = "__ftl_interpolation"; //$NON-NLS-1$

	public final static String[] FREEMARKER_PARTITION_TYPES = new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			IFreemarkerPartitions.FREEMARKER_STRING,
			IFreemarkerPartitions.FREEMARKER_COMMENT, FTL_ASSIGN, FTL_INCLUDE,
			FTL_INTERPOLATION, FTL_IF_DIRECTIVE_START, FTL_LIST_DIRECTIVE_START };

	public final static String[] LEGAL_CONTENT_TYPES = new String[] {

	IFreemarkerPartitions.FREEMARKER_COMMENT,
			IFreemarkerPartitions.FREEMARKER_PARTITIONING,
			IFreemarkerPartitions.FREEMARKER_STRING, FTL_ASSIGN, FTL_INCLUDE,
			FTL_INTERPOLATION, FTL_IF_DIRECTIVE_START,
			FTL_LIST_DIRECTIVE_START, IDocument.DEFAULT_CONTENT_TYPE };

}
