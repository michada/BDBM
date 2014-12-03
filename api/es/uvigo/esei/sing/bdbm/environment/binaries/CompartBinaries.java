package es.uvigo.esei.sing.bdbm.environment.binaries;

public interface CompartBinaries extends Binaries {
	public final static String COMPART_BINARIES_PREFIX = "compart.";
	
	public final static String BASE_DIRECTORY_PROP = 
		COMPART_BINARIES_PREFIX + "binDir";
	
	public final static String COMPART_PROP = 
		COMPART_BINARIES_PREFIX + "compart";
	
	public abstract String getCompart();
}
