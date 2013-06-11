package freemarker.provisionnal.debug.impl.rmi;

import java.rmi.RemoteException;


import freemarker.core.Environment;
import freemarker.provisionnal.debug.DebugModel;
import freemarker.provisionnal.debug.impl.IDebugModelFactory;
import freemarker.template.TemplateModel;

public class RmiDebugModelFactory implements IDebugModelFactory {

	public static final IDebugModelFactory INSTANCE = new RmiDebugModelFactory();
	
	public DebugModel newModel(TemplateModel model, int extraTypes)
			throws RemoteException {
		return new RmiDebugModelImpl(model, extraTypes, this);
	}
	
	public DebugModel newModel(Environment env) throws RemoteException {
		return new RmiDebuggedEnvironmentImpl(env, this);
	}

}
