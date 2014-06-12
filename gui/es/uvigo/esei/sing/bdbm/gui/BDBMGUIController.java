package es.uvigo.esei.sing.bdbm.gui;

import java.io.IOException;
import java.util.Observable;

import es.uvigo.esei.sing.bdbm.BDBMManager;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.BDBMEnvironment;
import es.uvigo.esei.sing.bdbm.gui.configuration.PathsConfiguration;

public class BDBMGUIController extends Observable {
	private final BDBMManager manager;

	public BDBMGUIController(BDBMManager manager) {
		this.manager = manager;
	}

	public BDBMManager getManager() {
		return manager;
	}

	public BDBMController getController() {
		return this.manager.getController();
	}

	public BDBMEnvironment getEnvironment() {
		return this.manager.getEnvironment();
	}

	public boolean isAccessionInferEnabled() {
		return this.manager.getEnvironment().isAccessionInferEnabled();
	}

	public boolean changePaths(PathsConfiguration configuration)
	throws IOException {
		if (this.getEnvironment().changePaths(
			configuration.getBaseRespository(), 
			configuration.getBaseBLAST(),
			configuration.getBaseEMBOSS()
		)) {
			
			this.setChanged();
			this.notifyObservers(configuration);
			
			return true;
		} else {
			return false;
		}
	}
}
