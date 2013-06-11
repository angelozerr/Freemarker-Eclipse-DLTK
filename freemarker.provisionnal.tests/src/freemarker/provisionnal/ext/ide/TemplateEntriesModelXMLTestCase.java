package freemarker.provisionnal.ext.ide;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TemplateEntriesModelXMLTestCase extends TestCase {

	public void test() {

		Map<String, Object> models = new HashMap<String, Object>();
		// user
		try {
			models.put("doc", freemarker.ext.dom.NodeModel
					.parse(new InputSource(TemplateEntriesModelXMLTestCase.class
							.getResourceAsStream("book.xml"))));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TemplateEntriesModel entriesModel = TemplateEntriesUtils
				.createEntriesModel(models);
		
		TemplateEntriesUtils.display(entriesModel);
		
	}
	
	

}
