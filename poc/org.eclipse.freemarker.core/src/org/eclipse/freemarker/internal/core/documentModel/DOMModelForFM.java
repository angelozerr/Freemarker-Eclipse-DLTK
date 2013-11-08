/*******************************************************************************
 * Copyright (c) 2013 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.freemarker.internal.core.documentModel;

import org.eclipse.freemarker.internal.core.documentModel.dom.DOMDocumentForFM;
import org.eclipse.freemarker.internal.core.documentModel.dom.FMDOMModelParser;
import org.eclipse.freemarker.internal.core.documentModel.dom.FMDOMModelUpdater;
import org.eclipse.wst.html.core.internal.document.DOMStyleModelImpl;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.XMLModelParser;
import org.eclipse.wst.xml.core.internal.document.XMLModelUpdater;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Document;

/*
 * The PHPModel will support both the DOM style interface and Freemarker specific API's.
 */
public class DOMModelForFM extends DOMStyleModelImpl {

	/*
	 * This is modeled after what is done for JSP
	 */
	protected Document internalCreateDocument() {
		DOMDocumentForFM document = new DOMDocumentForFM();
		document.setModel(this);
		return document;
	}

	protected XMLModelParser createModelParser() {
		return new FMDOMModelParser(this);
	}

	protected XMLModelUpdater createModelUpdater() {
		return new FMDOMModelUpdater(this);
	}
	@Override
	public IndexedRegion getIndexedRegion(int offset) {
		IndexedRegion result = super.getIndexedRegion(offset);
		if(result == null && offset == getDocument().getEndOffset()){
			return (IDOMNode) getDocument().getLastChild();
		}
		return super.getIndexedRegion(offset);
	}
}
