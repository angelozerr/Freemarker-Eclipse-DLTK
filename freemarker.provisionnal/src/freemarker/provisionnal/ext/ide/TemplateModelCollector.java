package freemarker.provisionnal.ext.ide;

import freemarker.template.TemplateModel;

public interface TemplateModelCollector {

	void collect(TemplateModel modelKey, TemplateModel modelValue, String paths[]);
}
