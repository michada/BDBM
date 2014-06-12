package es.uvigo.esei.sing.bdbm.environment;

public interface NCBIEnvironment {
	public abstract boolean isValidFor(String osName);
	public abstract String getDefaultSplign();
	public abstract String getDefaultCompart();
	public abstract String getDefaultBedtools();
}
