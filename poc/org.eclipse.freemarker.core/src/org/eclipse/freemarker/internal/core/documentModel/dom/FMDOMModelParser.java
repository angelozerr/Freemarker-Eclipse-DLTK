/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *******************************************************************************/
package org.eclipse.freemarker.internal.core.documentModel.dom;

import org.eclipse.freemarker.internal.core.documentModel.parser.FMRegionContext;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.document.DOMModelImpl;
import org.eclipse.wst.xml.core.internal.document.XMLModelParser;

public class FMDOMModelParser extends XMLModelParser {

	public static final String PHP_TAG_NAME = "PHP"; //$NON-NLS-1$

	public FMDOMModelParser(DOMModelImpl model) {
		super(model);
	}

	protected boolean isNestedContent(String regionType) {
		return regionType == FMRegionContext.PHP_CONTENT;
	}

	protected boolean isNestedTag(String regionType) {
		return regionType == FMRegionContext.PHP_OPEN
				|| regionType == FMRegionContext.PHP_CLOSE;
	}

	protected boolean isNestedTagOpen(String regionType) {
		return regionType == FMRegionContext.PHP_OPEN;
	}

	protected String computeNestedTag(String regionType, String tagName,
			IStructuredDocumentRegion structuredDocumentRegion,
			ITextRegion region) {
		return FMDOMModelParser.PHP_TAG_NAME;
	}

	protected boolean isNestedTagClose(String regionType) {
		return regionType == FMRegionContext.PHP_CLOSE;
	}
}
