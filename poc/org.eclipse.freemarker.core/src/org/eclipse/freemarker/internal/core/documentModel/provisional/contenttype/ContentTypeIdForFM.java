package org.eclipse.freemarker.internal.core.documentModel.provisional.contenttype;

import org.eclipse.freemarker.internal.core.FMCorePlugin;

public class ContentTypeIdForFM {
	/**
	 * The value of the contenttype id field must match what is specified in
	 * plugin.xml file. Note: this value is intentially set with default
	 * protected method so it will not be inlined.
	 */
	public final static String ContentTypeID_PHP = getConstantString();

	/**
	 * Don't allow instantiation.
	 */
	private ContentTypeIdForFM() {
		super();
	}

	static String getConstantString() {
		return FMCorePlugin.ID + ".fmsource"; //$NON-NLS-1$
	}
}
