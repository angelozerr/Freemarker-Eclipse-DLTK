package freemarker.provisionnal.debug.impl.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;


import freemarker.provisionnal.debug.DebugModel;
import freemarker.provisionnal.debug.impl.BaseDebugModelImpl;
import freemarker.provisionnal.debug.impl.IDebugModelFactory;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author Attila Szegedi
 * @version $Id: RmiDebugModelImpl.java,v 1.2.2.1 2006/11/27 07:54:49 szegedia
 *          Exp $
 */
class RmiDebugModelImpl extends UnicastRemoteObject implements DebugModel {

	private BaseDebugModelImpl delegate = null;

	private static final long serialVersionUID = 1L;

	RmiDebugModelImpl(TemplateModel model, int extraTypes, IDebugModelFactory modelFactory)
			throws RemoteException {
		super();
		delegate = new BaseDebugModelImpl(model, extraTypes, modelFactory);
	}

	public String getAsString() throws TemplateModelException {
		return delegate.getAsString();
	}

	public Number getAsNumber() throws TemplateModelException {
		return delegate.getAsNumber();
	}

	public Date getAsDate() throws TemplateModelException {
		return delegate.getAsDate();
	}

	public int getDateType() {
		return delegate.getDateType();
	}

	public boolean getAsBoolean() throws TemplateModelException {
		return delegate.getAsBoolean();
	}

	public int size() throws TemplateModelException {
		return delegate.size();
	}

	public DebugModel get(int index) throws TemplateModelException,
			RemoteException {
		return delegate.get(index);
	}

	public DebugModel[] get(int fromIndex, int toIndex)
			throws TemplateModelException, RemoteException {
		return delegate.get(fromIndex, toIndex);
	}

	public DebugModel[] getCollection() throws TemplateModelException,
			RemoteException {
		return delegate.getCollection();
	}

	public DebugModel get(String key) throws TemplateModelException,
			RemoteException {
		return delegate.get(key);
	}

	public DebugModel[] get(String[] keys) throws TemplateModelException,
			RemoteException {
		return delegate.get(keys);
	}

	public String[] keys() throws TemplateModelException {
		return delegate.keys();
	}

	public int getModelTypes() {
		return delegate.getModelTypes();
	}
}
