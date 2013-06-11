package freemarker.provisionnal.ext.ide;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import freemarker.provisionnal.ext.ide.model.Product;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;

public class TemplateEntriesFindDataModelTestCase extends TestCase {

	public void testFindDataModel() throws Exception {

		Map<String, Object> models = new HashMap<String, Object>();
		Product latestProduct = new Product();
		latestProduct.setUrl("products/greenmouse.html");
		latestProduct.setName("green mouse");
		models.put("latestProduct", latestProduct);

		TemplateHashModelEx hashModel = new SimpleHash(models);
		TemplateEntriesModel entriesModel = new SimpleEntries(hashModel);

		String[] paths = { "latestProduct" };
		entriesModel.findDataModel(paths, new TemplateModelCollector() {

			public void collect(TemplateModel modelKey,
					TemplateModel modelValue, String[] paths) {
				System.out.println("key=" + modelKey + ", value=" + modelValue);
			}
		});

	}

	public void testFindDataModelField() throws Exception {

		Map<String, Object> models = new HashMap<String, Object>();
		Product latestProduct = new Product();
		latestProduct.setUrl("products/greenmouse.html");
		latestProduct.setName("green mouse");
		models.put("latestProduct", latestProduct);

		TemplateHashModelEx hashModel = new SimpleHash(models);
		TemplateEntriesModel entriesModel = new SimpleEntries(hashModel);

		String[] paths = { "latestProduct", "u" };
		entriesModel.findDataModel(paths, new TemplateModelCollector() {

			public void collect(TemplateModel modelKey,
					TemplateModel modelValue, String[] paths) {
				// key=url, indexPath=1, value=products/greenmouse.html
				System.out.println("key=" + modelKey + ", value=" + modelValue);

				assertEquals(modelKey.toString(), "url");
				assertEquals(modelValue.toString(), "products/greenmouse.html");
			}
		});

	}
}
