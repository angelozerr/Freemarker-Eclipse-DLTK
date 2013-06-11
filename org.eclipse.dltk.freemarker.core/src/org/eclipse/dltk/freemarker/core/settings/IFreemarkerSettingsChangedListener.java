package org.eclipse.dltk.freemarker.core.settings;

import java.util.EventListener;

/**
 * A listener, which gets notified when settings* have changed.
 */
public interface IFreemarkerSettingsChangedListener extends EventListener {

	/**
	 * Notifies that the given event has occurred.
	 * 
	 * @param event
	 *            the change event
	 */
	public void settingsChanged(IFreemarkerSettingsChangedEvent event);
}
