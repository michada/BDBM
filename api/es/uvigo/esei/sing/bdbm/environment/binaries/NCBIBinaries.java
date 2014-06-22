package es.uvigo.esei.sing.bdbm.environment.binaries;


public interface NCBIBinaries extends Binaries {
	public final static String NCBI_BINARIES_PREFIX = "ncbi.";
	
	public final static String BASE_DIRECTORY_PROP = 
		NCBI_BINARIES_PREFIX + "binDir";
	
	public final static String SPLIGN_PROP = 
		NCBI_BINARIES_PREFIX + "splign";
	
	public final static String COMPART_PROP = 
		NCBI_BINARIES_PREFIX + "compart";
	
	public final static String BEDTOOLS_PROP = 
		NCBI_BINARIES_PREFIX + "bedtools";
	
	public abstract String getSplign();
	public abstract String getCompart();
	public abstract String getBedtools();
}
