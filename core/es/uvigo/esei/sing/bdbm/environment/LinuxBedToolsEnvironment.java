package es.uvigo.esei.sing.bdbm.environment;

public class LinuxBedToolsEnvironment implements BedToolsEnvironment {
	@Override
	public boolean isValidFor(String osName) {
		return osName.toLowerCase().contains("linux");
	}

	@Override
	public String getDefaultBedtools() {
		return "bedtools";
	}
}
