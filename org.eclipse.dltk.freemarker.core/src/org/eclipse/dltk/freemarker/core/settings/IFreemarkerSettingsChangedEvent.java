package org.eclipse.dltk.freemarker.core.settings;

public interface IFreemarkerSettingsChangedEvent {

	public enum EventType {
		TEMPLATE_SETTINGS_SAVED, PROJECT_SETTINGS_SAVED
	};
	
	IFreemarkerElementSettings getSettings();
	
	EventType getType();
}
