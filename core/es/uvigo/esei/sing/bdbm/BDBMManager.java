package es.uvigo.esei.sing.bdbm;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.BDBMEnvironment;
import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.BinaryCheckException;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.NCBIBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.NCBIBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.persistence.BDBMRepositoryManager;

public class BDBMManager {
	private final BDBMEnvironment environment;
	private final BDBMController controller;
	private final BDBMRepositoryManager repositoryManager;
	
	public BDBMManager(
		BDBMEnvironment environment, 
		BDBMRepositoryManager repositoryManager,
		BDBMController controller
	) throws IOException, BinaryCheckException {
		this.environment = environment;
		this.repositoryManager = repositoryManager;
		
		repositoryManager.setRepositoryPaths(environment.getRepositoryPaths());
		
		this.controller = controller;
		this.controller.setRepositoryManager(this.repositoryManager);
		this.controller.setBlastBinariesExecutor(
			createBLASTBinariesExecutor(this.getEnvironment().getBLASTBinaries())
		);
		this.controller.setEmbossBinariesExecutor(
			createEMBOSSBinariesExecutor(this.getEnvironment().getEMBOSSBinaries())
		);
	}
	
	private BLASTBinariesExecutor createBLASTBinariesExecutor(BLASTBinaries binaries)
	throws BinaryCheckException {
		return BLASTBinaryToolsFactoryBuilder.newFactory(binaries)
			.createExecutor();
	}
	
	private EMBOSSBinariesExecutor createEMBOSSBinariesExecutor(EMBOSSBinaries binaries)
	throws BinaryCheckException {
		return EMBOSSBinaryToolsFactoryBuilder.newFactory(binaries)
			.createExecutor();
	}
	
	private NCBIBinariesExecutor createNCBIBinariesExecutor(NCBIBinaries binaries)
	throws BinaryCheckException {
		return NCBIBinaryToolsFactoryBuilder.newFactory(binaries)
			.createExecutor();
	}
	
	public BDBMEnvironment getEnvironment() {
		return environment;
	}

	public BDBMController getController() {
		return controller;
	}

	public BDBMRepositoryManager getRepositoryManager() {
		return repositoryManager;
	}
	
	public boolean checkBLASTPath(String path) {
		try {
			this.createBLASTBinariesExecutor(
				this.getEnvironment().createBLASTBinaries(path)
			);
			return true;
		} catch (BinaryCheckException e) {
			return false;
		}
	}
	
	public boolean checkEMBOSSPath(String path) {
		try {
			this.createEMBOSSBinariesExecutor(
				this.getEnvironment().createEMBOSSBinaries(path)
			);
			return true;
		} catch (BinaryCheckException e) {
			return false;
		}
	}

	public boolean checkNCBIPath(String path) {
		try {
			this.createNCBIBinariesExecutor(
				this.getEnvironment().createNCBIBinaries(path)
			);
			return true;
		} catch (BinaryCheckException e) {
			return false;
		}
	}

	public void shutdown() {
		this.repositoryManager.shutdown();
	}
}
