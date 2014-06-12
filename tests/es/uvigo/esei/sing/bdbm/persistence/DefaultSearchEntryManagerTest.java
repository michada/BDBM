package es.uvigo.esei.sing.bdbm.persistence;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.TestDirectory;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public class DefaultSearchEntryManagerTest extends AbstractRepositoryManagerTest<DefaultSearchEntryRepositoryManager> {
	@Override
	protected DefaultSearchEntryRepositoryManager newManager() {
		return new DefaultSearchEntryRepositoryManager();
	}
	
	@Override
	protected void expectations(RepositoryPaths pathsMock) {
		expect(this.pathsMock.getSearchEntryNucleotidesDirectory())
			.andReturn(TestDirectory.NUCLEOTIDE_ENTRY_DIRECTORY)
			.anyTimes();
		expect(this.pathsMock.getSearchEntryProteinsDirectory())
			.andReturn(TestDirectory.PROTEIN_ENTRY_DIRECTORY)
			.anyTimes();
	}

	@Test
	@Ignore("Not yet implemented")
	public void testListSearchEntries() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExistsSearchEntry() {
		fail("Not yet implemented");
	}
	
	@Test
	@Ignore("Not yet implemented")
	public void testListProteinSearchEntries() {
		assertArrayEquals(
			create().protein().searchEntry(),
			this.manager.listProtein()
		);
	}

	@Test
	public void testListNucleotideSearchEntries() {
		assertArrayEquals(
			create().nucleotide().searchEntry(),
			this.manager.listNucleotide()
		);
	}

	@Test
	public void testExistsProteinSearchEntry() {
		checkProteinExists(
			TestDirectory.VALID_DATA_NAMES, 
			TestDirectory.INVALID_DATA_NAMES
		);
	}

	@Test
	public void testExistsNucleotideSearchEntry() {
		checkNucleotideExists(
			TestDirectory.VALID_DATA_NAMES, 
			TestDirectory.INVALID_DATA_NAMES
		);
	}
}
