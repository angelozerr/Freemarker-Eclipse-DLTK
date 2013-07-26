package org.eclipse.dltk.freemarker.internal.core.parser;

import org.eclipse.dltk.compiler.problem.IProblemIdentifier;
import org.eclipse.dltk.freemarker.core.FreemarkerCorePlugin;

public enum FreemarkerParserProblems implements IProblemIdentifier {

	INTERNAL_ERROR;

	public String contributor() {
		return FreemarkerCorePlugin.PLUGIN_ID;
	}

}
