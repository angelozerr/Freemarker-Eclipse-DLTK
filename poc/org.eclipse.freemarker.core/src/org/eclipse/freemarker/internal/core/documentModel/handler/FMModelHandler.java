package org.eclipse.freemarker.internal.core.documentModel.handler;

import org.eclipse.freemarker.internal.core.documentModel.encoding.FMDocumentCharsetDetector;
import org.eclipse.freemarker.internal.core.documentModel.loader.FMDocumentLoader;
import org.eclipse.freemarker.internal.core.documentModel.loader.FMModelLoader;
import org.eclipse.freemarker.internal.core.documentModel.provisional.contenttype.ContentTypeIdForFM;
import org.eclipse.wst.sse.core.internal.document.IDocumentCharsetDetector;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.ltk.modelhandler.AbstractModelHandler;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;

public class FMModelHandler extends AbstractModelHandler {

	/**
	 * Needs to match what's in plugin registry. In fact, can be overwritten at
	 * run time with what's in registry! (so should never be 'final')
	 */
	private static String ModelHandlerID = "org.eclipse.php.core.documentModel.handler"; //$NON-NLS-1$

	public FMModelHandler() {
		super();
		setId(ModelHandlerID);
		setAssociatedContentTypeId(ContentTypeIdForFM.ContentTypeID_PHP);
	}

	public IModelLoader getModelLoader() {
		return new FMModelLoader();
	}

	public IDocumentCharsetDetector getEncodingDetector() {
		return new FMDocumentCharsetDetector();
	}

	public IDocumentLoader getDocumentLoader() {
		return new FMDocumentLoader();
	}
}
