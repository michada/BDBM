package es.uvigo.esei.sing.bdbm.environment;

import java.util.ServiceLoader;

import es.uvigo.esei.sing.bdbm.environment.EMBOSSEnvironment;

public class EMBOSSEnvironmentFactory {
	private final static ServiceLoader<EMBOSSEnvironment> SERVICE_LOADER = 
		ServiceLoader.load(EMBOSSEnvironment.class);
	
	public static EMBOSSEnvironment createEnvironment() {
		for (EMBOSSEnvironment environment : SERVICE_LOADER) {
			if (environment.isValidFor(System.getProperty("os.name"))) {
				return environment;
			}
		}
		
		return null;
	}
}
