package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;

public class BLASTBinaryToolsFactoryBuilder {
	private final static ServiceLoader<BLASTBinaryToolsFactory> SERVICE_LOADER = 
		ServiceLoader.load(BLASTBinaryToolsFactory.class);
	
	public static BLASTBinaryToolsFactory newFactory(BLASTBinaries bBinaries)
	throws BinaryCheckException {
		BLASTBinaryToolsFactory selectedFactory = null;
		
		for (BLASTBinaryToolsFactory factory : SERVICE_LOADER) {
			if (factory.isValidFor(bBinaries)) {
				selectedFactory = factory;
				SERVICE_LOADER.reload();
				
				break;
			}
		}
		
		if (selectedFactory == null) {
			selectedFactory = new DefaultBLASTBinaryToolsFactory();
		}
		selectedFactory.setBinaries(bBinaries);
		
		return selectedFactory;
	}
}
