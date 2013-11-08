package org.eclipse.freemarker.internal.ui.editor.highlighter;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.internal.provisional.style.AbstractLineStyleProvider;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;

/**
 * Coloring mechanism for Freemarker partitions
 */
public class LineStyleProviderForFM extends AbstractLineStyleProvider implements
		LineStyleProvider {

	@Override
	protected TextAttribute getAttributeFor(ITextRegion arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IPreferenceStore getColorPreferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadColors() {
		// TODO Auto-generated method stub

	}
}
