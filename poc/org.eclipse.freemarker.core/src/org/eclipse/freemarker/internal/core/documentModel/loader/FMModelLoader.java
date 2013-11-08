package org.eclipse.freemarker.internal.core.documentModel.loader;

import java.util.List;

import org.eclipse.freemarker.internal.core.documentModel.DOMModelForFM;
import org.eclipse.wst.html.core.internal.encoding.HTMLModelLoader;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

public class FMModelLoader extends HTMLModelLoader {

	public IDocumentLoader getDocumentLoader() {
		if (documentLoaderInstance == null) {
			documentLoaderInstance = new FMDocumentLoader();
		}
		return documentLoaderInstance;
	}

	public IModelLoader newInstance() {
		return new FMModelLoader();
	}

	public List getAdapterFactories() {

		// @GINO: Might want to add new adapter factories here
		return super.getAdapterFactories();
	}

	// Creating the FMModel
	public IStructuredModel newModel() {
		return new DOMModelForFM();
	}

}
