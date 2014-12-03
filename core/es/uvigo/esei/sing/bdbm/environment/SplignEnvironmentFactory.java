package es.uvigo.esei.sing.bdbm.environment;

import java.util.ServiceLoader;

public class SplignEnvironmentFactory {
	private final static ServiceLoader<SplignEnvironment> SERVICE_LOADER = 
		ServiceLoader.load(SplignEnvironment.class);
	
	public static SplignEnvironment createEnvironment() {
		for (SplignEnvironment environment : SERVICE_LOADER) {
			if (environment.isValidFor(System.getProperty("os.name"))) {
				return environment;
			}
		}
		
		return null;
	}
}
