package es.uvigo.esei.sing.bdbm.environment;

public interface BedToolsEnvironment {
	public abstract boolean isValidFor(String osName);
	public abstract String getDefaultBedtools();
}
