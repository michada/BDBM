package es.uvigo.esei.sing.bdbm.environment;

public interface SplignEnvironment {
	public abstract boolean isValidFor(String osName);
	public abstract String getDefaultSplign();
}
