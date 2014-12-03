package es.uvigo.esei.sing.bdbm.environment;

import java.util.ServiceLoader;

public class CompartEnvironmentFactory {
	private final static ServiceLoader<CompartEnvironment> SERVICE_LOADER = 
		ServiceLoader.load(CompartEnvironment.class);
	
	public static CompartEnvironment createEnvironment() {
		for (CompartEnvironment environment : SERVICE_LOADER) {
			if (environment.isValidFor(System.getProperty("os.name"))) {
				return environment;
			}
		}
		
		return null;
	}
}
