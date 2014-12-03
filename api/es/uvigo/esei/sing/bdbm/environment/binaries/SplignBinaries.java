package es.uvigo.esei.sing.bdbm.environment.binaries;


public interface SplignBinaries extends Binaries {
	public final static String SPLIGN_BINARIES_PREFIX = "splign.";
	
	public final static String BASE_DIRECTORY_PROP = 
		SPLIGN_BINARIES_PREFIX + "binDir";
	
	public final static String SPLIGN_PROP = 
		SPLIGN_BINARIES_PREFIX + "splign";
	
	public abstract String getSplign();
}
