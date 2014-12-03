package es.uvigo.esei.sing.bdbm.environment;

public class LinuxCompartEnvironment implements CompartEnvironment {
	@Override
	public boolean isValidFor(String osName) {
		return osName.toLowerCase().contains("linux");
	}

	@Override
	public String getDefaultCompart() {
		return "compart";
	}

}
