package freemarker.provisionnal.debug.impl;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import freemarker.cache.CacheStorage;
import freemarker.cache.SoftCacheStorage;
import freemarker.core.Configurable;
import freemarker.core.Environment;
import freemarker.ext.util.IdentityHashMap;
import freemarker.provisionnal.debug.DebugModel;
import freemarker.provisionnal.debug.DebuggedEnvironment;
import freemarker.template.Configuration;
import freemarker.template.SimpleCollection;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.UndeclaredThrowableException;

/**
 * @author Attila Szegedi
 * @version $Id: RmiDebuggedEnvironmentImpl.java,v 1.3.2.2 2006/11/27 07:55:17 szegedia Exp $
 */
public class BaseDebuggedEnvironmentImpl
extends
    BaseDebugModelImpl
implements
    DebuggedEnvironment
{
    private static final long serialVersionUID = 1L;

    private static final CacheStorage storage = new SoftCacheStorage(new IdentityHashMap());
    private static final Object idLock = new Object();
    private static long nextId = 1;
    
    private boolean stopped = false;
    private final long id;
    
    private Environment env;
    
    public BaseDebuggedEnvironmentImpl(Environment env, IDebugModelFactory modelFactory) throws RemoteException
    {
        super(new DebugEnvironmentModel(env, modelFactory), DebugModel.TYPE_ENVIRONMENT, modelFactory);
        synchronized(idLock)
        {
            id = nextId++;
        }
        this.env = env;
    }

    static synchronized Object getCachedWrapperFor(Object key, IDebugModelFactory modelFactory)
    throws
        RemoteException
    {
        Object value = storage.get(key);
        if(value == null)
        {
        	if(key instanceof Environment)
            {
                value = modelFactory.newModel((Environment)key);//new RmiDebugModelImpl((TemplateModel)key, extraTypes);
                //new BaseDebuggedEnvironmentImpl((Environment)key, modelFactory); 
            } else if(key instanceof TemplateModel)
            {
                int extraTypes;
                if(key instanceof DebugConfigurationModel)
                {
                    extraTypes = DebugModel.TYPE_CONFIGURATION;
                }
                else if(key instanceof DebugTemplateModel)
                {
                    extraTypes = DebugModel.TYPE_TEMPLATE;
                }
                else
                {
                    extraTypes = 0;
                }
                value = modelFactory.newModel((TemplateModel)key, extraTypes);//new RmiDebugModelImpl((TemplateModel)key, extraTypes);
            }          
            else if(key instanceof Template)
            {
                value = new DebugTemplateModel((Template)key, modelFactory);
            }
            else if(key instanceof Configuration)
            {
                value = new DebugConfigurationModel((Configuration)key, modelFactory);
            }
        }
        return value;
    }

    public void resume()
    {
        synchronized(this)
        {
            notify();
        }
    }

    public void stop()
    {
        stopped = true;
        resume();
    }

    public long getId()
    {
        return id;
    }
    
    public boolean isStopped()
    {
        return stopped;
    }
    
    private abstract static class DebugMapModel implements TemplateHashModelEx
    {
        public int size()
        {
            return keySet().size();
        }

        public TemplateCollectionModel keys()
        {
            return new SimpleCollection(keySet());
        }

        public TemplateCollectionModel values() throws TemplateModelException
        {
            Collection keys = keySet();
            List list = new ArrayList(keys.size());
            
            for(Iterator it = keys.iterator(); it.hasNext();)
            {
                list.add(get((String)it.next()));
            }
            return new SimpleCollection(list);
        }

        public boolean isEmpty()
        {
            return size() == 0;
        }
        
        abstract Collection keySet();

        static List composeList(Collection c1, Collection c2)
        {
            List list = new ArrayList(c1);
            list.addAll(c2);
            Collections.sort(list);
            return list;
        }
    }
    
    private static class DebugConfigurableModel extends DebugMapModel
    {
        static final List KEYS = Arrays.asList(new String[]
        {
            Configurable.ARITHMETIC_ENGINE_KEY,
            Configurable.BOOLEAN_FORMAT_KEY,
            // AZ : uncomment that with Freemarker 2.3 Configurable.CLASSIC_COMPATIBLE_KEY,
            Configurable.LOCALE_KEY,
            Configurable.NUMBER_FORMAT_KEY,
            Configurable.OBJECT_WRAPPER_KEY,
            Configurable.TEMPLATE_EXCEPTION_HANDLER_KEY
        });

        final Configurable configurable;
        
        protected final IDebugModelFactory modelFactory;
        
        DebugConfigurableModel(Configurable configurable, IDebugModelFactory modelFactory)
        {
            this.configurable = configurable;
            this.modelFactory = modelFactory;
        }
        
        Collection keySet()
        {
            return KEYS;
        }
        
        public TemplateModel get(String key) throws TemplateModelException
        {
            String s = configurable.getSetting(key);
            return s == null ? null : new SimpleScalar(s);
        }

    }
    
    private static class DebugConfigurationModel extends DebugConfigurableModel
    {
        private static final List KEYS = composeList(DebugConfigurableModel.KEYS, Collections.singleton("sharedVariables"));

        private TemplateModel sharedVariables = new DebugMapModel()
        {
            Collection keySet()
            {
                return ((Configuration)configurable).getSharedVariableNames();
            }
        
            public TemplateModel get(String key)
            {
                return ((Configuration)configurable).getSharedVariable(key);
            }
        };
        
        DebugConfigurationModel(Configuration config, IDebugModelFactory modelFactory)
        {
            super(config, modelFactory);
        }
        
        Collection keySet()
        {
            return KEYS;
        }

        public TemplateModel get(String key) throws TemplateModelException
        {
            if("sharedVariables".equals(key))
            {
                return sharedVariables; 
            }
            else
            {
                return super.get(key);
            }
        }
    }
    
    private static class DebugTemplateModel extends DebugConfigurableModel
    {
        private static final List KEYS = composeList(DebugConfigurableModel.KEYS, 
            Arrays.asList(new String[] {
                "configuration", 
                "name",
                }));
    
        private final SimpleScalar name;

        DebugTemplateModel(Template template, IDebugModelFactory modelFactory)
        {
            super(template, modelFactory);
            this.name = new SimpleScalar(template.getName());
        }

        Collection keySet()
        {
            return KEYS;
        }

        public TemplateModel get(String key) throws TemplateModelException
        {
            if("configuration".equals(key))
            {
                try
                {
                    return (TemplateModel)getCachedWrapperFor(((Template)configurable).getConfiguration(), modelFactory);
                }
                catch (RemoteException e)
                {
                    throw new TemplateModelException(e);
                }
            }
            if("name".equals(key))
            {
                return name;
            }
            return super.get(key);
        }
    }

    public static class DebugEnvironmentModel extends DebugConfigurableModel
    {
        private static final List KEYS = composeList(DebugConfigurableModel.KEYS, 
            Arrays.asList(new String[] {
                "currentNamespace",
                "dataModel",
                "globalNamespace",
                "knownVariables",
                "mainNamespace",
                "template",
                 }));
    
        private TemplateModel knownVariables = new DebugMapModel()
        {
            Collection keySet()
            {
                try
                {
                    return ((Environment)configurable).getKnownVariableNames();
                }
                catch (TemplateModelException e)
                {
                    throw new UndeclaredThrowableException(e);
                }
            }
        
            public TemplateModel get(String key) throws TemplateModelException
            {
                return ((Environment)configurable).getVariable(key);
            }
        };
		
        public DebugEnvironmentModel(Environment env, IDebugModelFactory modelFactory)
        {
            super(env, modelFactory);
        }

        Collection keySet()
        {
            return KEYS;
        }

        public TemplateModel get(String key) throws TemplateModelException
        {
            if("currentNamespace".equals(key))
            {
                return ((Environment)configurable).getCurrentNamespace();
            }
            if("dataModel".equals(key))
            {
                return ((Environment)configurable).getDataModel();
            }
            if("globalNamespace".equals(key))
            {
                return ((Environment)configurable).getGlobalNamespace();
            }
            if("knownVariables".equals(key))
            {
                return knownVariables;
            }
            if("mainNamespace".equals(key))
            {
                return ((Environment)configurable).getMainNamespace();
            }
            if("template".equals(key))
            {
                try
                {
                    return (TemplateModel) getCachedWrapperFor(((Environment)configurable).getTemplate(), modelFactory);
                }
                catch (RemoteException e)
                {
                    throw new TemplateModelException(e);
                }
            }
            return super.get(key);
        }
    }
    
    public DebugModel getVariable(String varName) throws RemoteException {  
    	try {
			TemplateModel model =  env.getVariable(varName);
			if (model != null) {
				return (DebugModel)getCachedWrapperFor(model, modelFactory);
			}
			
    	} catch (TemplateModelException e) {
			throw new RemoteException("", e);
		}
    	return null;
    }
    
    public Collection<String> getGlobalVariableNames() throws RemoteException {    
    	try {
			return env.getDirectVariableNames();
		} catch (TemplateModelException e) {
			throw new RemoteException("", e);
		}
    }
    
    public Collection<String> getLocalVariableNames() throws RemoteException {
    	try {
			return env.getCurrentScope().getDirectVariableNames();
		} catch (TemplateModelException e) {
			throw new RemoteException("", e);
		}
    }
}
