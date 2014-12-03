package es.uvigo.esei.sing.bdbm;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.BDBMEnvironment;
import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.BedToolsBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.SplignBinaries;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.BedToolsBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.BedToolsBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.BinaryCheckException;
import es.uvigo.esei.sing.bdbm.environment.execution.CompartBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.CompartBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.SplignBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.SplignBinaryToolsFactoryBuilder;
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
		this.controller.setBedToolsBinariesExecutor(
			createBedToolsBinariesExecutor(this.getEnvironment().getBedToolsBinaries())
		);
		this.controller.setSplignBinariesExecutor(
			createSplignBinariesExecutor(this.getEnvironment().getSplignBinaries())
		);
		this.controller.setCompartBinariesExecutor(
			createCompartBinariesExecutor(this.getEnvironment().getCompartBinaries())
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
	
	private BedToolsBinariesExecutor createBedToolsBinariesExecutor(BedToolsBinaries binaries)
	throws BinaryCheckException {
		return BedToolsBinaryToolsFactoryBuilder.newFactory(binaries)
			.createExecutor();
	}
	
	private SplignBinariesExecutor createSplignBinariesExecutor(SplignBinaries binaries)
	throws BinaryCheckException {
		return SplignBinaryToolsFactoryBuilder.newFactory(binaries)
			.createExecutor();
	}
	
	private CompartBinariesExecutor createCompartBinariesExecutor(CompartBinaries binaries)
	throws BinaryCheckException {
		return CompartBinaryToolsFactoryBuilder.newFactory(binaries)
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

	public boolean checkBedToolsPath(String path) {
		try {
			this.createBedToolsBinariesExecutor(
				this.getEnvironment().createBedToolsBinaries(path)
			);
			return true;
		} catch (BinaryCheckException e) {
			return false;
		}
	}

	public boolean checkSplignPath(String path) {
		try {
			this.createSplignBinariesExecutor(
				this.getEnvironment().createSplignBinaries(path)
			);
			return true;
		} catch (BinaryCheckException e) {
			return false;
		}
	}

	public boolean checkCompartPath(String path) {
		try {
			this.createCompartBinariesExecutor(
				this.getEnvironment().createCompartBinaries(path)
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
