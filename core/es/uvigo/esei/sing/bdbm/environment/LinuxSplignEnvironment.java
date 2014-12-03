package es.uvigo.esei.sing.bdbm.environment;

public class LinuxSplignEnvironment implements SplignEnvironment {
	@Override
	public boolean isValidFor(String osName) {
		return osName.toLowerCase().contains("linux");
	}

	@Override
	public String getDefaultSplign() {
		return "splign";
	}
}
