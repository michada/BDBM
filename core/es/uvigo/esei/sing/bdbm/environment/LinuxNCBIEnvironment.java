package es.uvigo.esei.sing.bdbm.environment;

public class LinuxNCBIEnvironment implements NCBIEnvironment {
	@Override
	public boolean isValidFor(String osName) {
		return osName.toLowerCase().contains("linux");
	}

	@Override
	public String getDefaultSplign() {
		return "splign";
	}

	@Override
	public String getDefaultCompart() {
		return "compart";
	}

	@Override
	public String getDefaultBedtools() {
		return "bedtools";
	}
}
