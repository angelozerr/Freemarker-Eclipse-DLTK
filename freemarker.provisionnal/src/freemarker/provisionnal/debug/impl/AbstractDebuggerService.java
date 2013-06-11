/*******************************************************************************
 * Copyright (c) 2010 Freemarker Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package freemarker.provisionnal.debug.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import freemarker.core.Environment;
import freemarker.core.ast.TemplateElement;
import freemarker.provisionnal.core.ast.DebugBreak;
import freemarker.provisionnal.debug.Breakpoint;
import freemarker.provisionnal.debug.DebuggerListener;
import freemarker.provisionnal.debug.EnvironmentSuspendedEvent;
import freemarker.template.Template;

/**
 * Abstract class for debugger service.
 * 
 */
public abstract class AbstractDebuggerService implements DebuggerService {

	private final Map templateDebugInfos = new HashMap();
	private final HashSet suspendedEnvironments = new HashSet();
	private final Map listeners = new HashMap();
	private final ReferenceQueue refQueue = new ReferenceQueue();

	private IDebugModelFactory modelFactory;

	public AbstractDebuggerService() {
		this(DefaultDebugModelFactory.INSTANCE);
	}

	public AbstractDebuggerService(IDebugModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	public List getBreakpoints(String templateName) {
		synchronized (templateDebugInfos) {
			TemplateDebugInfo tdi = findTemplateDebugInfo(templateName);
			return tdi == null ? Collections.EMPTY_LIST : tdi.breakpoints;
		}
	}

	public List getBreakpoints() {
		List sumlist = new ArrayList();
		synchronized (templateDebugInfos) {
			for (Iterator iter = templateDebugInfos.values().iterator(); iter
					.hasNext();) {
				sumlist.addAll(((TemplateDebugInfo) iter.next()).breakpoints);
			}
		}
		Collections.sort(sumlist);
		return sumlist;
	}

	public boolean suspendEnvironment(Environment env, int line,
			String templateName, String fileName) throws RemoteException {
		BaseDebuggedEnvironmentImpl denv = (BaseDebuggedEnvironmentImpl) BaseDebuggedEnvironmentImpl
				.getCachedWrapperFor(env, modelFactory);

		synchronized (suspendedEnvironments) {
			suspendedEnvironments.add(denv);
		}
		try {
			EnvironmentSuspendedEvent breakpointEvent = new EnvironmentSuspendedEvent(
					this, line, templateName, fileName, denv);

			synchronized (listeners) {
				for (Iterator iter = listeners.values().iterator(); iter
						.hasNext();) {
					DebuggerListener listener = (DebuggerListener) iter.next();
					listener.environmentSuspended(breakpointEvent);
				}
			}
			synchronized (denv) {
				try {
					denv.wait();
				} catch (InterruptedException e) {
					;// Intentionally ignored
				}
			}
			return denv.isStopped();
		} finally {
			synchronized (suspendedEnvironments) {
				suspendedEnvironments.remove(denv);
			}
		}
	}

	public void registerTemplate(Template template) {
		String templateName = template.getName();
		synchronized (templateDebugInfos) {
			TemplateDebugInfo tdi = createTemplateDebugInfo(templateName);
			tdi.templates.add(new TemplateReference(templateName, template,
					refQueue));
			// Inject already defined breakpoints into the template
			for (Iterator iter = tdi.breakpoints.iterator(); iter.hasNext();) {
				Breakpoint breakpoint = (Breakpoint) iter.next();
				insertDebugBreak(template, breakpoint);
			}
		}
	}

	public Collection getSuspendedEnvironments() {
		return (Collection) suspendedEnvironments.clone();
	}

	public Object addDebuggerListener(DebuggerListener listener) {
		Object id;
		synchronized (listeners) {
			id = new Long(System.currentTimeMillis());
			listeners.put(id, listener);
		}
		return id;
	}

	public void removeDebuggerListener(Object id) {
		synchronized (listeners) {
			listeners.remove(id);
		}
	}

	public void addBreakpoint(Breakpoint breakpoint) {
		String templateName = breakpoint.getTemplateName();
		synchronized (templateDebugInfos) {
			TemplateDebugInfo tdi = createTemplateDebugInfo(templateName);
			List breakpoints = tdi.breakpoints;
			int pos = Collections.binarySearch(breakpoints, breakpoint);
			if (pos < 0) {
				// Add to the list of breakpoints
				breakpoints.add(-pos - 1, breakpoint);
				// Inject the breakpoint into all templates with this name
				for (Iterator iter = tdi.templates.iterator(); iter.hasNext();) {
					TemplateReference ref = (TemplateReference) iter.next();
					Template t = ref.getTemplate();
					if (t == null) {
						iter.remove();
					} else {
						insertDebugBreak(t, breakpoint);
					}
				}
			}
		}
	}

	private void insertDebugBreak(Template t, Breakpoint breakpoint) {
		TemplateElement te = findTemplateElement(t.getRootTreeNode(),
				breakpoint.getLine());
		if (te == null) {
			return;
		}
		TemplateElement parent = (TemplateElement) te.getParent();

		DebugBreak db = new DebugBreak(te, this, breakpoint);
		// TODO: Ensure there always is a parent by making sure
		// that the root element in the template is always a MixedContent
		// Also make sure it doesn't conflict with anyone's code.
		parent.setChildAt(parent.getIndex(te), db);
	}

	private static TemplateElement findTemplateElement(TemplateElement te,
			int line) {
		if (te.getBeginLine() > line || te.getEndLine() < line) {
			return null;
		}

		// Find the narrowest match
		for (Enumeration children = te.children(); children.hasMoreElements();) {
			TemplateElement child = (TemplateElement) children.nextElement();
			TemplateElement childmatch = findTemplateElement(child, line);
			if (childmatch != null) {
				return childmatch;
			}
		}
		// If no child provides narrower match, return this
		return te;
	}

	private TemplateDebugInfo findTemplateDebugInfo(String templateName) {
		processRefQueue();
		return (TemplateDebugInfo) templateDebugInfos.get(templateName);
	}

	private TemplateDebugInfo createTemplateDebugInfo(String templateName) {
		TemplateDebugInfo tdi = findTemplateDebugInfo(templateName);
		if (tdi == null) {
			tdi = new TemplateDebugInfo();
			templateDebugInfos.put(templateName, tdi);
		}
		return tdi;
	}

	public void removeBreakpoint(Breakpoint breakpoint) {
		String templateName = breakpoint.getTemplateName();
		synchronized (templateDebugInfos) {
			TemplateDebugInfo tdi = findTemplateDebugInfo(templateName);
			if (tdi != null) {
				List breakpoints = tdi.breakpoints;
				int pos = Collections.binarySearch(breakpoints, breakpoint);
				if (pos >= 0) {
					breakpoints.remove(pos);
					for (Iterator iter = tdi.templates.iterator(); iter
							.hasNext();) {
						TemplateReference ref = (TemplateReference) iter.next();
						Template t = ref.getTemplate();
						if (t == null) {
							iter.remove();
						} else {
							removeDebugBreak(t, breakpoint);
						}
					}
				}
				if (tdi.isEmpty()) {
					templateDebugInfos.remove(templateName);
				}
			}
		}
	}

	private void removeDebugBreak(Template t, Breakpoint breakpoint) {
		TemplateElement te = findTemplateElement(t.getRootTreeNode(),
				breakpoint.getLine());
		if (te == null) {
			return;
		}
		DebugBreak db = null;
		while (te != null) {
			if (te instanceof DebugBreak) {
				db = (DebugBreak) te;
				break;
			}
			te = (TemplateElement) te.getParent();
		}
		if (db == null) {
			return;
		}
		TemplateElement parent = (TemplateElement) db.getParent();
		parent.setChildAt(parent.getIndex(db), (TemplateElement) db
				.getChildAt(0));
	}

	public void removeBreakpoints(String templateName) {
		synchronized (templateDebugInfos) {
			TemplateDebugInfo tdi = findTemplateDebugInfo(templateName);
			if (tdi != null) {
				removeBreakpoints(tdi);
				if (tdi.isEmpty()) {
					templateDebugInfos.remove(templateName);
				}
			}
		}
	}

	public void removeBreakpoints() {
		synchronized (templateDebugInfos) {
			for (Iterator iter = templateDebugInfos.values().iterator(); iter
					.hasNext();) {
				TemplateDebugInfo tdi = (TemplateDebugInfo) iter.next();
				removeBreakpoints(tdi);
				if (tdi.isEmpty()) {
					iter.remove();
				}
			}
		}
	}

	private void removeBreakpoints(TemplateDebugInfo tdi) {
		tdi.breakpoints.clear();
		for (Iterator iter = tdi.templates.iterator(); iter.hasNext();) {
			TemplateReference ref = (TemplateReference) iter.next();
			Template t = ref.getTemplate();
			if (t == null) {
				iter.remove();
			} else {
				removeDebugBreaks(t.getRootTreeNode());
			}
		}
	}

	private void removeDebugBreaks(TemplateElement te) {
		int count = te.getChildCount();
		for (int i = 0; i < count; ++i) {
			TemplateElement child = (TemplateElement) te.getChildAt(i);
			while (child instanceof DebugBreak) {
				TemplateElement dbchild = (TemplateElement) child.getChildAt(0);
				te.setChildAt(i, dbchild);
				child = dbchild;
			}
			removeDebugBreaks(child);
		}
	}

	private static final class TemplateDebugInfo {
		final List templates = new ArrayList();
		final List breakpoints = new ArrayList();

		boolean isEmpty() {
			return templates.isEmpty() && breakpoints.isEmpty();
		}
	}

	private static final class TemplateReference extends WeakReference {
		final String templateName;

		TemplateReference(String templateName, Template template,
				ReferenceQueue queue) {
			super(template, queue);
			this.templateName = templateName;
		}

		Template getTemplate() {
			return (Template) get();
		}
	}

	private void processRefQueue() {
		for (;;) {
			TemplateReference ref = (TemplateReference) refQueue.poll();
			if (ref == null) {
				break;
			}
			TemplateDebugInfo tdi = findTemplateDebugInfo(ref.templateName);
			if (tdi != null) {
				tdi.templates.remove(ref);
				if (tdi.isEmpty()) {
					templateDebugInfos.remove(ref.templateName);
				}
			}
		}
	}

}
