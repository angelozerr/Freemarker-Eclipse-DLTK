package freemarker.provisionnal.ext.ide;

import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Template entries model which store list of the keys/values of a
 * {@link TemplateHashModelEx}.
 */
public interface TemplateEntriesModel extends TemplateModel {

	/**
	 * Return the entries of a {@link TemplateHashModelEx}.
	 * 
	 * @return
	 * @throws TemplateModelException
	 */
	TemplateEntryModel[] entries() throws TemplateModelException;

	/**
	 * Find the Data-Model which match paths. For each {@link TemplateModel}
	 * matched,
	 * {@link TemplateModelCollector#collect(TemplateModel, TemplateModel, String[], int)}
	 * is called.
	 * 
	 * @param paths
	 * @param collector
	 * @throws TemplateModelException
	 */
	void findDataModel(String[] paths, TemplateModelCollector collector)
			throws TemplateModelException;
}
