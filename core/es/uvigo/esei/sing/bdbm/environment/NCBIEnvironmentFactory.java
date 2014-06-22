package es.uvigo.esei.sing.bdbm.environment;

import java.util.ServiceLoader;

public class NCBIEnvironmentFactory {
	private final static ServiceLoader<NCBIEnvironment> SERVICE_LOADER = 
		ServiceLoader.load(NCBIEnvironment.class);
	
	public static NCBIEnvironment createEnvironment() {
		for (NCBIEnvironment environment : SERVICE_LOADER) {
			if (environment.isValidFor(System.getProperty("os.name"))) {
				return environment;
			}
		}
		
		return null;
	}
}
