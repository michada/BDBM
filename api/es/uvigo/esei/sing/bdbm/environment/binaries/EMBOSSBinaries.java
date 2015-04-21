package es.uvigo.esei.sing.bdbm.environment.binaries;


public interface EMBOSSBinaries extends Binaries {
	public final static String EMBOSS_BINARIES_PREFIX = "emboss.";
	
	public final static String BASE_DIRECTORY_PROP = 
		EMBOSS_BINARIES_PREFIX + "binDir";
	
	public final static String GETORF_PROP = 
		EMBOSS_BINARIES_PREFIX + "getorf";
	
	public final static String REVSEQ_PROP = 
		EMBOSS_BINARIES_PREFIX + "revseq";
	
	public abstract String getGetORF();
	
	public abstract String getRevseq();
}
