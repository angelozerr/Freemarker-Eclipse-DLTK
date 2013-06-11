package freemarker.provisionnal.debug.impl.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;

import freemarker.provisionnal.debug.Breakpoint;
import freemarker.provisionnal.debug.Debugger;
import freemarker.provisionnal.debug.DebuggerListener;



/**
 * @author Attila Szegedi
 * @version $Id: RmiDebuggerImpl.java,v 1.2.2.1 2006/11/27 07:54:49 szegedia Exp $
 */
class RmiDebuggerImpl
extends
    UnicastRemoteObject
implements
    Debugger
{
    private static final long serialVersionUID = 1L;

    private final RmiDebuggerService service;
    
    protected RmiDebuggerImpl(RmiDebuggerService service) throws RemoteException
    {
        this.service = service;
    }

    public void addBreakpoint(Breakpoint breakpoint)
    {
        service.addBreakpoint(breakpoint);
    }

    public Object addDebuggerListener(DebuggerListener listener)
    {
        return service.addDebuggerListener(listener);
    }

    public List getBreakpoints()
    {
        return service.getBreakpoints();
    }

    public List getBreakpoints(String templateName)
    {
        return service.getBreakpoints(templateName);
    }

    public Collection getSuspendedEnvironments()
    {
        return service.getSuspendedEnvironments();
    }

    public void removeBreakpoint(Breakpoint breakpoint)
    {
        service.removeBreakpoint(breakpoint);
    }

    public void removeDebuggerListener(Object id)
    {
        service.removeDebuggerListener(id);
    }

    public void removeBreakpoints()
    {
        service.removeBreakpoints();
    }

    public void removeBreakpoints(String templateName)
    {
        service.removeBreakpoints(templateName);
    }
}
