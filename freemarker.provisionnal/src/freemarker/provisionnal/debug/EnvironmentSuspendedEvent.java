package freemarker.provisionnal.debug;

import java.util.EventObject;

import freemarker.template.TemplateModel;

/**
 * Event describing a suspension of an environment (ie because it hit a
 * breakpoint).
 * @author Attila Szegedi
 * @version $Id: EnvironmentSuspendedEvent.java,v 1.1.2.1 2006/11/27 07:54:19 szegedia Exp $
 */
public class EnvironmentSuspendedEvent extends EventObject
{
    private static final long serialVersionUID = 1L;

    private final int line;
    private final String templateName;
    private final String fileName;
    private final DebuggedEnvironment env;

    public EnvironmentSuspendedEvent(Object source, int line, String templateName, String fileName, DebuggedEnvironment env)
    {
        super(source);
        this.line = line;        
        this.templateName = templateName;
        this.fileName = fileName;
        this.env = env;
    }

    /**
     * The line number in the template where the execution of the environment
     * was suspended.
     * @return int the line number
     */
    public int getLine()
    {
        return line;
    }

    /**
     * The environment that was suspended
     * @return DebuggedEnvironment
     */
    public DebuggedEnvironment getEnvironment()
    {
        return env;
    }
    
    
    public String getTemplateName() {
		return templateName;
	}

	public String getFileName() {
		return fileName;
	}

}
