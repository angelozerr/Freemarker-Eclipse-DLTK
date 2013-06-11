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

import org.eclipse.dltk.freemarker.internal.ui.text.scanners.FreemarkerPartitionScanner;
import org.eclipse.dltk.ui.text.IPartitioningProvider;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * Provider for Freemarker partionning.
 * 
 */
public class FreemarkerPartitioningProvider implements IPartitioningProvider {

	public static IPartitioningProvider getInstance() {
		return INSTANCE;
	}

	private static final FreemarkerPartitioningProvider INSTANCE = new FreemarkerPartitioningProvider();

	private FreemarkerPartitioningProvider() {
	}

	public IPartitionTokenScanner createPartitionScanner() {
		return new FreemarkerPartitionScanner();
	}

	public String[] getPartitionContentTypes() {
		return IFreemarkerPartitions.FREEMARKER_PARTITION_TYPES;
	}

	public String getPartitioning() {
		return IFreemarkerPartitions.FREEMARKER_PARTITIONING;
	}

}
