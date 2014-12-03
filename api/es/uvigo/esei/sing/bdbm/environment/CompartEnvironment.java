package es.uvigo.esei.sing.bdbm.environment;

public interface CompartEnvironment {
	public abstract boolean isValidFor(String osName);
	public abstract String getDefaultCompart();
}
