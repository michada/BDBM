package es.uvigo.esei.sing.bdbm.cli.commands;

import es.uvigo.esei.sing.bdbm.controller.BDBMController;

public abstract class BDBMCommand extends AbstractOptionCommand {
	protected final BDBMController controller;

	public BDBMCommand(BDBMController controller) {
		this.controller = controller;
	}
}
