package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;

public class NCBIBinaryToolsFactoryBuilder {
	private final static ServiceLoader<NCBIBinaryToolsFactory> SERVICE_LOADER = 
		ServiceLoader.load(NCBIBinaryToolsFactory.class);
	
	public static NCBIBinaryToolsFactory newFactory(NCBIBinaries nBinaries)
	throws BinaryCheckException {
		NCBIBinaryToolsFactory selectedFactory = null;
		
		for (NCBIBinaryToolsFactory factory : SERVICE_LOADER) {
			if (factory.isValidFor(nBinaries)) {
				selectedFactory = factory;
				SERVICE_LOADER.reload();
				
				break;
			}
		}
		
		if (selectedFactory == null) {
			selectedFactory = new DefaultNCBIBinaryToolsFactory();
		}
		selectedFactory.setBinaries(nBinaries);
		
		return selectedFactory;
	}
}
