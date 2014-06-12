package es.uvigo.esei.sing.bdbm.environment;

import es.uvigo.esei.sing.bdbm.environment.EMBOSSEnvironment;

public class LinuxEMBOSSEnvironment implements EMBOSSEnvironment {
	@Override
	public boolean isValidFor(String osName) {
		return osName.toLowerCase().contains("linux");
	}

	@Override
	public String getDefaultGetORF() {
		return "getorf";
	}
}
