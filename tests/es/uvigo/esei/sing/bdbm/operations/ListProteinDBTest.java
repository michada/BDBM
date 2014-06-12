package es.uvigo.esei.sing.bdbm.operations;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.BinaryCheckException;
import es.uvigo.esei.sing.bdbm.environment.paths.DefaultRepositoryPaths;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;
import es.uvigo.esei.sing.bdbm.operations.protein.ListProteinsDB;
import es.uvigo.esei.sing.bdbm.persistence.TestDirectoryTest;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultProteinDatabase;

public class ListProteinDBTest extends TestDirectoryTest {
	private final static File TEST_BASE_DIRECTORY = new File("test");
	
	private ListProteinsDB listProteinDB;
	private RepositoryPaths repositoryPaths;
	
	@Before
	public void setUp() throws Exception {
		this.repositoryPaths = new DefaultRepositoryPaths(TEST_BASE_DIRECTORY);
		this.listProteinDB = new ListProteinsDB();
	}

	@After
	public void tearDown() throws Exception {
		this.repositoryPaths = null;
		this.listProteinDB = null;
	}

	@Test
	public void testListProteins() throws BinaryCheckException {
		final Database[] proteinDBs = this.listProteinDB.list();
		Arrays.sort(proteinDBs);
		
		final File dbDirectory = new File(TEST_BASE_DIRECTORY, DefaultRepositoryPaths.DB_DIRECTORY_NAME);
		final File protDBDirectory = new File(dbDirectory, SequenceType.PROTEIN.getDirectoryExtension());
		
		assertArrayEquals(
			new Database[] { 
				new DefaultProteinDatabase(new File(protDBDirectory, "CG13575")), 
				new DefaultProteinDatabase(new File(protDBDirectory, "input"))
			},
			proteinDBs
		);
	}
}
