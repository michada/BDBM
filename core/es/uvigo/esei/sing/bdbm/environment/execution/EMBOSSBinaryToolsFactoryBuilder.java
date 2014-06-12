package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;

public class EMBOSSBinaryToolsFactoryBuilder {
	private final static ServiceLoader<EMBOSSBinaryToolsFactory> SERVICE_LOADER = 
		ServiceLoader.load(EMBOSSBinaryToolsFactory.class);
	
	public static EMBOSSBinaryToolsFactory newFactory(EMBOSSBinaries eBinaries)
	throws BinaryCheckException {
		EMBOSSBinaryToolsFactory selectedFactory = null;
		
		for (EMBOSSBinaryToolsFactory factory : SERVICE_LOADER) {
			if (factory.isValidFor(eBinaries)) {
				selectedFactory = factory;
				SERVICE_LOADER.reload();
				
				break;
			}
		}
		
		if (selectedFactory == null) {
			selectedFactory = new DefaultEMBOSSBinaryToolsFactory();
		}
		selectedFactory.setBinaries(eBinaries);
		
		return selectedFactory;
	}
}
