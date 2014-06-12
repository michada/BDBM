package es.uvigo.esei.sing.bdbm.persistence;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.TestDirectory;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public class DefaultDatabaseManagerTest extends AbstractRepositoryManagerTest<DefaultDatabaseRepositoryManager> {
	@Override
	protected DefaultDatabaseRepositoryManager newManager() {
		return new DefaultDatabaseRepositoryManager();
	}
	
	@Override
	protected void expectations(RepositoryPaths pathsMock) {
		expect(this.pathsMock.getDBNucleotidesDirectory())
			.andReturn(TestDirectory.NUCLEOTIDE_DB_DIRECTORY)
			.anyTimes();
		expect(this.pathsMock.getDBProteinsDirectory())
			.andReturn(TestDirectory.PROTEIN_DB_DIRECTORY)
			.anyTimes();
	}
	
	@Test
	@Ignore("Not yet implemented")
	public void testCreateDatabase() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testListDatabases() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExistsDB() {
		fail("Not yet implemented");
	}

	@Test
	public void testListProteinDatabases() throws IOException {
		assertArrayEquals(
			create().protein().db(),
			this.manager.listProtein()
		);
	}

	@Test
	public void testListNucleotideDBs() {
		assertArrayEquals(
			create().nucleotide().db(),
			this.manager.listNucleotide()
		);
	}

	@Test
	public void testExistsProteinDB() {
		checkProteinExists(
			TestDirectory.VALID_DATA_NAMES, 
			TestDirectory.INVALID_DATA_NAMES
		);
	}

	@Test
	public void testExistsNucleotideDB() {
		checkNucleotideExists(
			TestDirectory.VALID_DATA_NAMES, 
			TestDirectory.INVALID_DATA_NAMES
		);
	}
}
