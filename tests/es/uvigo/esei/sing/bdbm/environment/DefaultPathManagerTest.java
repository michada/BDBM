package es.uvigo.esei.sing.bdbm.environment;


import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.paths.DefaultRepositoryPaths;

public class DefaultPathManagerTest {
	private static final File BASE_DIRECTORY = new File("test");
	private static final File ALTERNATIVE_BASE_DIRECTORY = new File("alternative");
	
	private DefaultRepositoryPaths pathManager;

	@Before
	public void setUp() throws Exception {
		this.pathManager = new DefaultRepositoryPaths(
			DefaultPathManagerTest.BASE_DIRECTORY
		);
	}

	@After
	public void tearDown() throws Exception {
		this.pathManager = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new DefaultRepositoryPaths(null);
	}
	
	@Test
	public void testDefaultDirectories() {
		this.testDataDirectories(BASE_DIRECTORY);
	}
	
	@Test
	public void testBaseDirectoryChange() {
		this.pathManager.setBaseDirectory(ALTERNATIVE_BASE_DIRECTORY);
		
		this.testDataDirectories(ALTERNATIVE_BASE_DIRECTORY);
	}
	
	@Test
	public void testDataDirectoriesChange() {
		File directory = new File("dbnucl");
		this.pathManager.setDBNucleotidesDirectory(directory);
		assertEquals(directory, this.pathManager.getDBNucleotidesDirectory());
		
		directory = new File("dbprot");
		this.pathManager.setDBProteinsDirectory(directory);
		assertEquals(directory, this.pathManager.getDBProteinsDirectory());
		
		directory = new File("fastanucl");
		this.pathManager.setFastaNucleotidesDirectory(directory);
		assertEquals(directory, this.pathManager.getFastaNucleotidesDirectory());
		
		directory = new File("fastaprot");
		this.pathManager.setFastaProteinsDirectory(directory);
		assertEquals(directory, this.pathManager.getFastaProteinsDirectory());
		
		directory = new File("entrynucl");
		this.pathManager.setEntryNucleotidesDirectory(directory);
		assertEquals(directory, this.pathManager.getSearchEntryNucleotidesDirectory());
		
		directory = new File("entryprot");
		this.pathManager.setEntryProteinsDirectory(directory);
		assertEquals(directory, this.pathManager.getSearchEntryProteinsDirectory());
		
		directory = new File("exportnucl");
		this.pathManager.setExportNucleotidesDirectory(directory);
		assertEquals(directory, this.pathManager.getExportNucleotidesDirectory());
		
		directory = new File("exportprot");
		this.pathManager.setExportProteinsDirectory(directory);
		assertEquals(directory, this.pathManager.getExportProteinsDirectory());
	}
	
	@Test
	public void testBaseDirectoryChangeDataAfterDataDirectoryChange() {
		this.testDataDirectoriesChange();
		
		this.testBaseDirectoryChange();
	}

	protected void testDataDirectories(final File baseDirectory) {
		// DB
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.DB_DIRECTORY_NAME),
				SequenceType.PROTEIN.getDirectoryExtension()
			),
			this.pathManager.getDBProteinsDirectory()
		);
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.DB_DIRECTORY_NAME),
				SequenceType.NUCLEOTIDE.getDirectoryExtension()
			),
			this.pathManager.getDBNucleotidesDirectory()
		);
		
		// Fasta
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.FASTA_DIRECTORY_NAME),
				SequenceType.PROTEIN.getDirectoryExtension()
			),
			this.pathManager.getFastaProteinsDirectory()
		);
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.FASTA_DIRECTORY_NAME),
				SequenceType.NUCLEOTIDE.getDirectoryExtension()
			),
			this.pathManager.getFastaNucleotidesDirectory()
		);
		
		// Entry
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.ENTRY_DIRECTORY_NAME),
				SequenceType.PROTEIN.getDirectoryExtension()
			),
			this.pathManager.getSearchEntryProteinsDirectory()
		);
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.ENTRY_DIRECTORY_NAME),
				SequenceType.NUCLEOTIDE.getDirectoryExtension()
			),
			this.pathManager.getSearchEntryNucleotidesDirectory()
		);
		
		// Export
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.EXPORT_DIRECTORY_NAME),
				SequenceType.PROTEIN.getDirectoryExtension()
			),
			this.pathManager.getExportProteinsDirectory()
		);
		assertEquals(
			new File(
				new File(baseDirectory, DefaultRepositoryPaths.EXPORT_DIRECTORY_NAME),
				SequenceType.NUCLEOTIDE.getDirectoryExtension()
			),
			this.pathManager.getExportNucleotidesDirectory()
		);
	}
}
