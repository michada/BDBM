package es.uvigo.esei.sing.bdbm.environment;

public interface BLASTEnvironment {
	public abstract boolean isValidFor(String osName);
	public abstract String getDefaultMakeBlastDB();
	public abstract String getDefaultBlastDBAliasTool();
	public abstract String getDefaultBlastDBCmd();
	public abstract String getDefaultBlastN();
	public abstract String getDefaultBlastP();
	public abstract String getDefaultTBlastX();
	public abstract String getDefaultTBlastN();
}
