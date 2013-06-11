package freemarker.provisionnal.ext.ide;

import java.util.Map;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class TemplateEntriesUtils {

	public static TemplateEntriesModel createEntriesModel(
			Map<String, Object> models) {
		TemplateHashModelEx hashModel = new SimpleHash(models);
		return new SimpleEntries(hashModel);
	}

	public static void display(TemplateEntriesModel entriesModel) {
		display(entriesModel, 0);
	}
	
	public static void display(TemplateEntriesModel entriesModel, int indent) {

		try {

			TemplateEntryModel[] entries = entriesModel.entries();
			for (int i = 0; i < entries.length; i++) {
				TemplateEntryModel entry = entries[i];
				display(entry, indent);
			}

		} catch (TemplateModelException e) {
			e.printStackTrace();
		}

	}

	public static void display(TemplateEntryModel entryModel, int indent) {
		// Key
		String key = entryModel.getKey().toString();
		if ("class".equals(key))
			return;
				
		System.out.println();		
		for (int i = 0; i < indent; i++) {
			System.out.print("\t");
		}
				
		System.out.print(key);
		System.out.print("=");

		// Value
		TemplateModel value = entryModel.getValue();
		if (value instanceof TemplateEntriesModel) {
			display((TemplateEntriesModel) value, ++indent);	
		} else {
			System.out.print(value);
		}
	}
}
