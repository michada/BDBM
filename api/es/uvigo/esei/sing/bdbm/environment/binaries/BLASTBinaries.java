package es.uvigo.esei.sing.bdbm.environment.binaries;


public interface BLASTBinaries extends Binaries {
	public final static String BLAST_BINARIES_PREFIX = "blast.";
	
	public final static String BASE_DIRECTORY_PROP = 
		BLAST_BINARIES_PREFIX + "binDir";
	
	public final static String MAKE_BLAST_DB_PROP = 
		BLAST_BINARIES_PREFIX + "makeblastdb";
	public final static String BLASTDB_ALIASTOOL_PROP = 
		BLAST_BINARIES_PREFIX + "blastdb_aliastool";
	public final static String BLASTDB_CMD_PROP = 
		BLAST_BINARIES_PREFIX + "blastdbcmd";
	public final static String BLAST_N_PROP = 
		BLAST_BINARIES_PREFIX + "blastn";
	public final static String BLAST_P_PROP = 
		BLAST_BINARIES_PREFIX + "blastp";
	public final static String T_BLAST_X_PROP = 
		BLAST_BINARIES_PREFIX + "tblastx";
	public final static String T_BLAST_N_PROP = 
		BLAST_BINARIES_PREFIX + "tblastn";

	public abstract String getMakeBlastDB();
	public abstract String getBlastDBAliasTool();
	public abstract String getBlastDBCmd();
	public abstract String getBlastN();
	public abstract String getBlastP();
	public abstract String getTBlastX();
	public abstract String getTBlastN();
	public abstract String getBlast(BLASTType blastType);
}
