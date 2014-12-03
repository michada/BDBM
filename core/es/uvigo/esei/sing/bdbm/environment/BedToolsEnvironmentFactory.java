package es.uvigo.esei.sing.bdbm.environment;

import java.util.ServiceLoader;

public class BedToolsEnvironmentFactory {
	private final static ServiceLoader<BedToolsEnvironment> SERVICE_LOADER = 
		ServiceLoader.load(BedToolsEnvironment.class);
	
	public static BedToolsEnvironment createEnvironment() {
		for (BedToolsEnvironment environment : SERVICE_LOADER) {
			if (environment.isValidFor(System.getProperty("os.name"))) {
				return environment;
			}
		}
		
		return null;
	}
}
