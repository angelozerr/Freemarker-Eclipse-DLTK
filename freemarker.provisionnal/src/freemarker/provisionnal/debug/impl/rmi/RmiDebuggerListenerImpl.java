package freemarker.provisionnal.debug.impl.rmi;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;


import freemarker.log.Logger;
import freemarker.provisionnal.debug.DebuggerClient;
import freemarker.provisionnal.debug.DebuggerListener;
import freemarker.provisionnal.debug.EnvironmentSuspendedEvent;

/**
 * Used by the {@link DebuggerClient} to create local 
 * @author Attila Szegedi
 * @version $Id: RmiDebuggerListenerImpl.java,v 1.1.2.1 2006/11/27 07:54:49 szegedia Exp $
 */
public class RmiDebuggerListenerImpl
extends
    UnicastRemoteObject
implements
    DebuggerListener, Unreferenced
{
    private static final Logger logger = Logger.getLogger(
            "freemarker.debug.client");
    
    private static final long serialVersionUID = 1L;

    private final DebuggerListener listener;

    public void unreferenced()
    {
        try
        {
            UnicastRemoteObject.unexportObject(this, false);
        }
        catch (NoSuchObjectException e)
        {
            logger.warn("Failed to unexport RMI debugger listener", e);
        }
    }
    
    public RmiDebuggerListenerImpl(DebuggerListener listener) 
    throws RemoteException
    {
        this.listener = listener;
    }

    public void environmentSuspended(EnvironmentSuspendedEvent e) 
    throws RemoteException
    {
        listener.environmentSuspended(e);
    }
}
