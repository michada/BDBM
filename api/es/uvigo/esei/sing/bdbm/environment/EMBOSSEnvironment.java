package es.uvigo.esei.sing.bdbm.environment;

public interface EMBOSSEnvironment {
	public abstract boolean isValidFor(String osName);
	public abstract String getDefaultGetORF();
	public abstract String getDefaultRevseq();
}
