package org.eclipse.dltk.freemarker.internal.ui.text;

import org.eclipse.jface.text.rules.IWordDetector;

public class FreemarkerWordDetector implements IWordDetector {

	public boolean isWordPart(char character) {
		return Character.isJavaIdentifierPart(character);
	}

	public boolean isWordStart(char character) {
		return Character.isJavaIdentifierPart(character);
	}
}
