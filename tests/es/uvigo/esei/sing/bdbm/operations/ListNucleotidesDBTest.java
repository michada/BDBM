package es.uvigo.esei.sing.bdbm.operations;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.TestDirectory;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.BinaryCheckException;
import es.uvigo.esei.sing.bdbm.environment.paths.DefaultRepositoryPaths;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;
import es.uvigo.esei.sing.bdbm.operations.nucleotide.ListNucleotidesDB;
import es.uvigo.esei.sing.bdbm.persistence.TestDirectoryTest;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideDatabase;

public class ListNucleotidesDBTest extends TestDirectoryTest {
	private final static File TEST_BASE_DIRECTORY = new File("test");
	
	private ListNucleotidesDB listNucleotidesDB;
	private RepositoryPaths respositoryPaths;
	
	@Before
	public void setUp() throws Exception {
		this.respositoryPaths = new DefaultRepositoryPaths(TEST_BASE_DIRECTORY);
		this.listNucleotidesDB = new ListNucleotidesDB();
	}

	@After
	public void tearDown() throws Exception {
		this.respositoryPaths = null;
		this.listNucleotidesDB = null;
	}

	@Test
	public void testNucleotidesDB() throws BinaryCheckException {
		final Database[] nucleotidesDBs = this.listNucleotidesDB.list();
		Arrays.sort(nucleotidesDBs);
		
		final File dbDirectory = new File(TEST_BASE_DIRECTORY, DefaultRepositoryPaths.DB_DIRECTORY_NAME);
		final File nuclDBDirectory = new File(dbDirectory, SequenceType.NUCLEOTIDE.getDirectoryExtension());
		
		final File[] db = TestDirectory.list().valid().nucleotide().db();
		final Database[] expected = new Database[db.length];
		for (int i = 0; i < db.length; i++) {
			expected[i] = new DefaultNucleotideDatabase(db[i]);
		}
		
		assertArrayEquals(
			expected,
			nucleotidesDBs
		);
	}
}
