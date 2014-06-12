package es.uvigo.esei.sing.bdbm;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public final class TestDirectory {
	private static final File REPOSITORY = new File("repository");

	public static enum DirectoryType {
		DB("db"), SEARCH_ENTRY("entry"), EXPORT("export"), FASTA("fasta");
		
		private final String dirName;
		
		private DirectoryType(String dirName) {
			this.dirName = dirName;
		}
		
		public String getDirName() {
			return dirName;
		}
	}
	
	public final static String[] VALID_DATA_NAMES = new String[] { "CG13575", "input" };
	public final static String[] INVALID_DATA_NAMES = new String[] { "ABC", "123" };
	
	public final static File DIRECTORY = new File("test");
	
	public final static File DB_DIRECTORY = new File(DIRECTORY, DirectoryType.DB.getDirName());
	public final static File ENTRY_DIRECTORY = new File(DIRECTORY, DirectoryType.SEARCH_ENTRY.getDirName());
	public final static File EXPORT_DIRECTORY = new File(DIRECTORY, DirectoryType.EXPORT.getDirName());
	public final static File FASTA_DIRECTORY = new File(DIRECTORY, DirectoryType.FASTA.getDirName());
	
	public final static File PROTEIN_DB_DIRECTORY = new File(DB_DIRECTORY, SequenceType.PROTEIN.getDirectoryExtension());
	public final static File PROTEIN_ENTRY_DIRECTORY = new File(ENTRY_DIRECTORY, SequenceType.PROTEIN.getDirectoryExtension());
	public final static File PROTEIN_EXPORT_DIRECTORY = new File(EXPORT_DIRECTORY, SequenceType.PROTEIN.getDirectoryExtension());
	public final static File PROTEIN_FASTA_DIRECTORY = new File(FASTA_DIRECTORY, SequenceType.PROTEIN.getDirectoryExtension());
	
	public final static File NUCLEOTIDE_DB_DIRECTORY = new File(DB_DIRECTORY, SequenceType.NUCLEOTIDE.getDirectoryExtension());
	public final static File NUCLEOTIDE_ENTRY_DIRECTORY = new File(ENTRY_DIRECTORY, SequenceType.NUCLEOTIDE.getDirectoryExtension());
	public final static File NUCLEOTIDE_EXPORT_DIRECTORY = new File(EXPORT_DIRECTORY, SequenceType.NUCLEOTIDE.getDirectoryExtension());
	public final static File NUCLEOTIDE_FASTA_DIRECTORY = new File(FASTA_DIRECTORY, SequenceType.NUCLEOTIDE.getDirectoryExtension());
	
	private TestDirectory() {}
	
	public static void create() throws IOException {
		delete();
		FileUtils.copyDirectory(TestDirectory.REPOSITORY, DIRECTORY);
	}
	
	public static void delete() throws IOException {
		if (DIRECTORY.isDirectory())
			FileUtils.deleteDirectory(DIRECTORY);
	}
	
	public static List list() {
		return new List();
	}
	
	public final static class List {
		public ListSequence valid() {
			return new ListSequence(VALID_DATA_NAMES);
		}
		
		public ListSequence invalid() {
			return new ListSequence(INVALID_DATA_NAMES);
		}
	}
	
	public final static class ListSequence {
		private final String[] dataNames;
		
		private ListSequence(String[] dataNames) {
			this.dataNames = dataNames;
		}
		
		public ListSequenceDirectory protein() {
			return sequence(SequenceType.PROTEIN);
		}
		
		public ListSequenceDirectory nucleotide() {
			return sequence(SequenceType.NUCLEOTIDE);
		}

		public ListSequenceDirectory sequence(final SequenceType seqType) {
			return new ListSequenceDirectory(seqType, this.dataNames);
		}
	}
	
	public final static class ListSequenceDirectory {
		private final SequenceType sequenceType;
		private final String[] dataNames;
		
		private ListSequenceDirectory(SequenceType sequenceType,
				String[] dataNames) {
			this.sequenceType = sequenceType;
			this.dataNames = dataNames;
		}
		
		public File[] db() {
			return directory(DirectoryType.DB);
		}
		
		public File[] searchEntry() {
			return directory(DirectoryType.SEARCH_ENTRY);
		}
		
		public File[] export() {
			return directory(DirectoryType.EXPORT);
		}
		
		public File[] fasta() {
			return directory(DirectoryType.FASTA);
		}

		public File[] directory(final DirectoryType dirType) {
			return TestDirectory.listFiles(this.sequenceType, dirType, this.dataNames);
		}
	}
	
//	public static File[] listValidProteinDBFiles() {
//		return listValidFiles(SequenceType.PROTEIN, DirectoryType.DB);
//	}
//	
//	public static File[] listValidProteinFastaFiles() {
//		return listValidFiles(SequenceType.PROTEIN, DirectoryType.FASTA);
//	}
//	
//	public static File[] listValidProteinSearchEntryFiles() {
//		return listValidFiles(SequenceType.PROTEIN, DirectoryType.SEARCH_ENTRY);
//	}
//	
//	public static File[] listValidProteinExportFiles() {
//		return listValidFiles(SequenceType.PROTEIN, DirectoryType.EXPORT);
//	}
//	
//	public static File[] listValidNucleotideDBFiles() {
//		return listValidFiles(SequenceType.NUCLEOTIDE, DirectoryType.DB);
//	}
//	
//	public static File[] listValidNucleotideFastaFiles() {
//		return listValidFiles(SequenceType.NUCLEOTIDE, DirectoryType.FASTA);
//	}
//	
//	public static File[] listValidNucleotideSearchEntryFiles() {
//		return listValidFiles(SequenceType.NUCLEOTIDE, DirectoryType.SEARCH_ENTRY);
//	}
//	
//	public static File[] listValidNucleotideExportFiles() {
//		return listValidFiles(SequenceType.NUCLEOTIDE, DirectoryType.EXPORT);
//	}
//	
//	protected static File[] listValidFiles(SequenceType sequenceType, DirectoryType directoryType) {
//		return listFiles(sequenceType, directoryType, VALID_DATA_NAMES);
//	}
//	
//	protected static File[] listInvalidFiles(SequenceType sequenceType, DirectoryType directoryType) {
//		return listFiles(sequenceType, directoryType, INVALID_DATA_NAMES);
//	}

	protected static File[] listFiles(SequenceType sequenceType, DirectoryType directoryType, String[] dataNames) {
		final File baseDirectory = new File(
			new File(DIRECTORY, directoryType.getDirName()), 
			sequenceType.getDirectoryExtension()
		);
		
		final File[] db = new File[dataNames.length];
		
		int i = 0;
		for (String name : dataNames) {
			if (directoryType.equals(DirectoryType.FASTA))
				name += "." + sequenceType.getDBType() + ".fasta";
			
			db[i++] = new File(baseDirectory, name);
		}
		
		return db;
	}
	
}
