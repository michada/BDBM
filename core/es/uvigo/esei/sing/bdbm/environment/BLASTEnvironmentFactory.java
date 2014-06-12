package es.uvigo.esei.sing.bdbm.environment;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.BLASTEnvironment;

public class BLASTEnvironmentFactory {
	private final static ServiceLoader<BLASTEnvironment> SERVICE_LOADER = 
		ServiceLoader.load(BLASTEnvironment.class);
	
	public static BLASTEnvironment createEnvironment() {
		for (BLASTEnvironment bbn : SERVICE_LOADER) {
			if (bbn.isValidFor(System.getProperty("os.name"))) {
				return bbn;
			}
		}
		
		return null;
	}
}
