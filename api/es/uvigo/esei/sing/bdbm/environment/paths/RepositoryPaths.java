package es.uvigo.esei.sing.bdbm.environment.paths;

import java.io.File;
import java.io.IOException;

public interface RepositoryPaths {
	public final static String REPOSITORY_PREFIX = "repository.";
	public final static String DB_PREFIX = "db.";
	public final static String FASTA_PREFIX = "fasta.";
	public final static String SEARCH_ENTRY_PREFIX = "searchEntry.";
	public final static String EXPORT_PREFIX = "export.";
	public final static String ORF_PREFIX = "orf.";
	public final static String PROTEINS_PREFIX = "prot.";
	public final static String NUCLEOTIDES_PREFIX = "nucl.";
	
	public final static String BASE_DIRECTORY_PROP =
		REPOSITORY_PREFIX + "baseDir";
	
	public final static String DB_PROTEINS_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + DB_PREFIX + PROTEINS_PREFIX;
	public final static String DB_NUCLEOTIDES_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + DB_PREFIX + NUCLEOTIDES_PREFIX;
	
	public final static String FASTA_PROTEINS_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + FASTA_PREFIX + PROTEINS_PREFIX;
	public final static String FASTA_NUCLEOTIDES_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + FASTA_PREFIX + NUCLEOTIDES_PREFIX;
	
	public final static String ENTRY_PROTEINS_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + SEARCH_ENTRY_PREFIX + PROTEINS_PREFIX;
	public final static String ENTRY_NUCLEOTIDES_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + SEARCH_ENTRY_PREFIX + NUCLEOTIDES_PREFIX;
	
	public final static String EXPORT_PROTEINS_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + EXPORT_PREFIX + PROTEINS_PREFIX;
	public final static String EXPORT_NUCLEOTIDES_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + EXPORT_PREFIX + NUCLEOTIDES_PREFIX;
	
	public final static String ORF_NUCLEOTIDES_DIRECTORY_PROP = 
		REPOSITORY_PREFIX + ORF_PREFIX + NUCLEOTIDES_PREFIX;
	
	public abstract File getBaseDirectory();
	public abstract boolean checkBaseDirectory(File baseDirectory);
	public abstract void buildBaseDirectory(File baseDirectory) 
	throws IOException;
	
	public abstract File getDBProteinsDirectory();
	public abstract File getDBNucleotidesDirectory();
	public abstract File getFastaProteinsDirectory();
	public abstract File getFastaNucleotidesDirectory();
	public abstract File getSearchEntryProteinsDirectory();
	public abstract File getSearchEntryNucleotidesDirectory();
	public abstract File getExportProteinsDirectory();
	public abstract File getExportNucleotidesDirectory();
	public abstract File getORFNucleotidesDirectory();
}