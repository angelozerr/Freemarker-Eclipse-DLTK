package freemarker.provisionnal.debug.impl;

import java.rmi.RemoteException;


import freemarker.core.Environment;
import freemarker.provisionnal.debug.DebugModel;
import freemarker.template.TemplateModel;

public class DefaultDebugModelFactory implements IDebugModelFactory {

	public static final IDebugModelFactory INSTANCE = new DefaultDebugModelFactory();
	
	public DebugModel newModel(TemplateModel model, int extraTypes)
			throws RemoteException {
		return new BaseDebugModelImpl(model, extraTypes, this);
	}
	
	public DebugModel newModel(Environment env) throws RemoteException {
		return new BaseDebuggedEnvironmentImpl(env, this);
	}

}
