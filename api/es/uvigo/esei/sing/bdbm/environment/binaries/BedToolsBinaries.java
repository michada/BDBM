package es.uvigo.esei.sing.bdbm.environment.binaries;


public interface BedToolsBinaries extends Binaries {
	public final static String BEDTOOLS_BINARIES_PREFIX = "bedtools.";
	
	public final static String BASE_DIRECTORY_PROP = 
		BEDTOOLS_BINARIES_PREFIX + "binDir";
	
	public final static String BEDTOOLS_PROP = 
		BEDTOOLS_BINARIES_PREFIX + "bedtools";
	
	public abstract String getBedtools();
}
