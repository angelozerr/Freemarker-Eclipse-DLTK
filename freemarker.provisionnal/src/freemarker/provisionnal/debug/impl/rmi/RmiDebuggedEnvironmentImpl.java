package freemarker.provisionnal.debug.impl.rmi;

import java.rmi.RemoteException;
import java.util.Collection;


import freemarker.core.Environment;
import freemarker.provisionnal.debug.DebugModel;
import freemarker.provisionnal.debug.DebuggedEnvironment;
import freemarker.provisionnal.debug.impl.BaseDebuggedEnvironmentImpl;
import freemarker.provisionnal.debug.impl.IDebugModelFactory;

/**
 * @author Attila Szegedi
 * @version $Id: RmiDebuggedEnvironmentImpl.java,v 1.3.2.2 2006/11/27 07:55:17
 *          szegedia Exp $
 */
class RmiDebuggedEnvironmentImpl extends RmiDebugModelImpl implements
		DebuggedEnvironment {
	private BaseDebuggedEnvironmentImpl delegate = null;

	private static final long serialVersionUID = 1L;

	public  RmiDebuggedEnvironmentImpl(Environment env,
			IDebugModelFactory modelFactory) throws RemoteException {

		super(new BaseDebuggedEnvironmentImpl.DebugEnvironmentModel(env,
				modelFactory), DebugModel.TYPE_ENVIRONMENT, modelFactory);
		delegate = new BaseDebuggedEnvironmentImpl(env, modelFactory);
	}

	public void resume() {
		delegate.resume();
	}

	public void stop() {
		delegate.stop();
	}

	public long getId() {
		return delegate.getId();
	}

	public boolean isStopped() {
		return delegate.isStopped();
	}
	
	public DebugModel getVariable(String varName) throws RemoteException { 
		return delegate.getVariable(varName);
	}
	
	public Collection<String> getGlobalVariableNames() throws RemoteException {
		return delegate.getGlobalVariableNames();
	}
	
	public Collection<String> getLocalVariableNames() throws RemoteException {
		return delegate.getLocalVariableNames();
	}
}
