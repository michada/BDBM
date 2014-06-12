package es.uvigo.esei.sing.bdbm.gui.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import es.uvigo.esei.sing.bdbm.controller.BDBMController;

public abstract class BDBMAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final BDBMController controller;

	public BDBMAction(BDBMController controller) {
		this.controller = controller;
	}

	public BDBMAction(String name, BDBMController controller) {
		super(name);
		this.controller = controller;
	}

	public BDBMAction(String name, Icon icon, BDBMController controller) {
		super(name, icon);
		this.controller = controller;
	}
	
	protected BDBMController getController() {
		return this.controller;
	}
}
