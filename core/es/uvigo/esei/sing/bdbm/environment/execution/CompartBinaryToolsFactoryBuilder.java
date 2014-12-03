package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;

public class CompartBinaryToolsFactoryBuilder {
	private final static ServiceLoader<CompartBinaryToolsFactory> SERVICE_LOADER = 
		ServiceLoader.load(CompartBinaryToolsFactory.class);
	
	public static CompartBinaryToolsFactory newFactory(CompartBinaries nBinaries)
	throws BinaryCheckException {
		CompartBinaryToolsFactory selectedFactory = null;
		
		for (CompartBinaryToolsFactory factory : SERVICE_LOADER) {
			if (factory.isValidFor(nBinaries)) {
				selectedFactory = factory;
				SERVICE_LOADER.reload();
				
				break;
			}
		}
		
		if (selectedFactory == null) {
			selectedFactory = new DefaultCompartBinaryToolsFactory();
		}
		selectedFactory.setBinaries(nBinaries);
		
		return selectedFactory;
	}
}
