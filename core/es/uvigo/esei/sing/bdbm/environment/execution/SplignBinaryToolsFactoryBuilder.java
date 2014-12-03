package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.binaries.SplignBinaries;

public class SplignBinaryToolsFactoryBuilder {
	private final static ServiceLoader<SplignBinaryToolsFactory> SERVICE_LOADER = 
		ServiceLoader.load(SplignBinaryToolsFactory.class);
	
	public static SplignBinaryToolsFactory newFactory(SplignBinaries nBinaries)
	throws BinaryCheckException {
		SplignBinaryToolsFactory selectedFactory = null;
		
		for (SplignBinaryToolsFactory factory : SERVICE_LOADER) {
			if (factory.isValidFor(nBinaries)) {
				selectedFactory = factory;
				SERVICE_LOADER.reload();
				
				break;
			}
		}
		
		if (selectedFactory == null) {
			selectedFactory = new DefaultSplignBinaryToolsFactory();
		}
		selectedFactory.setBinaries(nBinaries);
		
		return selectedFactory;
	}
}
