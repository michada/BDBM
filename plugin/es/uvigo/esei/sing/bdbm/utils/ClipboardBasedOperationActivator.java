package es.uvigo.esei.sing.bdbm.utils;

import java.util.HashMap;
import java.util.Map.Entry;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.clipboard.ClipboardListener;

public class ClipboardBasedOperationActivator implements ClipboardListener {
	private final HashMap<String, HashMap<Class<?>, Integer>> operationRequirements = 
		new HashMap<String, HashMap<Class<?>, Integer>>();

	public void addRequirement(String uid, Class<?> c) {
		this.addRequirement(uid, c, 1);
	}
	
	public void addRequirement(String uid, Class<?> c, int count) {
		HashMap<Class<?>, Integer> reqs = this.operationRequirements.get(uid);
		
		if (reqs == null) {
			reqs = new HashMap<Class<?>, Integer>();
			operationRequirements.put(uid, reqs);
		}
		
		reqs.put(c, count);
	}

	private void processClipboard() {
		for (String uid : this.operationRequirements.keySet()) {
			boolean requirementsSatisfied = true;
			
			for (Entry<Class<?>, Integer> c : this.operationRequirements.get(uid).entrySet()) {
				if (Core.getInstance().getClipboard().getItemsByClass(c.getKey()).size() < c.getValue()) {
					requirementsSatisfied = false;
					break;
				}
			}
			
			if (requirementsSatisfied) {
				Core.getInstance().enableOperation(uid);
			} else {
				Core.getInstance().disableOperation(uid);
			}
		}

	}

	public void elementAdded(ClipboardItem arg0) {
		processClipboard();
	}

	public void elementRemoved(ClipboardItem arg0) {
		processClipboard();
	}
}
