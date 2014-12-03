package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.binaries.BedToolsBinaries;

public class BedToolsBinaryToolsFactoryBuilder {
	private final static ServiceLoader<BedToolsBinaryToolsFactory> SERVICE_LOADER = 
		ServiceLoader.load(BedToolsBinaryToolsFactory.class);
	
	public static BedToolsBinaryToolsFactory newFactory(BedToolsBinaries nBinaries)
	throws BinaryCheckException {
		BedToolsBinaryToolsFactory selectedFactory = null;
		
		for (BedToolsBinaryToolsFactory factory : SERVICE_LOADER) {
			if (factory.isValidFor(nBinaries)) {
				selectedFactory = factory;
				SERVICE_LOADER.reload();
				
				break;
			}
		}
		
		if (selectedFactory == null) {
			selectedFactory = new DefaultBedToolsBinaryToolsFactory();
		}
		selectedFactory.setBinaries(nBinaries);
		
		return selectedFactory;
	}
}
