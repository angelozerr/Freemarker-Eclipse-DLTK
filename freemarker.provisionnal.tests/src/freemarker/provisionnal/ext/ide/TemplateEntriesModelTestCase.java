package freemarker.provisionnal.ext.ide;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import freemarker.provisionnal.ext.ide.model.Product;

public class TemplateEntriesModelTestCase extends TestCase {

	public void test() {

		Map<String, Object> models = new HashMap<String, Object>();
		// user
		models.put("user", "Big JoeI");
		// latestProduct
		Product latestProduct = new Product();
		latestProduct.setUrl("products/greenmouse.html");
		latestProduct.setName("green mouse");
		models.put("latestProduct", latestProduct);
		
		TemplateEntriesModel entriesModel = TemplateEntriesUtils
				.createEntriesModel(models);
		
		TemplateEntriesUtils.display(entriesModel);
		
	}
	
	

}
