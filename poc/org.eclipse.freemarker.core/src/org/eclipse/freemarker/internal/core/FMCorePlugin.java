package org.eclipse.freemarker.internal.core;

import org.eclipse.core.runtime.Plugin;

public class FMCorePlugin extends Plugin {

	public static final String ID = "org.eclipse.freemarker.core"; //$NON-NLS-1$

	// The shared instance.
	private static FMCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public FMCorePlugin() {
		super();
		plugin = this;
	}

}
