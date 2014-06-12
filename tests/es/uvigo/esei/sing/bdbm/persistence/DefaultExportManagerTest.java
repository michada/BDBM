package es.uvigo.esei.sing.bdbm.persistence;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.TestDirectory;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public class DefaultExportManagerTest extends AbstractRepositoryManagerTest<DefaultExportRepositoryManager> {
	@Override
	protected DefaultExportRepositoryManager newManager() {
		return new DefaultExportRepositoryManager();
	}
	
	@Override
	protected void expectations(RepositoryPaths pathsMock) {
		expect(this.pathsMock.getExportNucleotidesDirectory())
			.andReturn(TestDirectory.NUCLEOTIDE_EXPORT_DIRECTORY)
			.anyTimes();
		expect(this.pathsMock.getExportProteinsDirectory())
			.andReturn(TestDirectory.PROTEIN_EXPORT_DIRECTORY)
			.anyTimes();
	}

	@Test
	@Ignore("Not yet implemented")
	public void testListExports() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExistsExport() {
		fail("Not yet implemented");
	}

	@Test
	public void testListProteinExports() {
		assertArrayEquals(
			create().protein().export(),
			this.manager.listProtein()
		);
	}

	@Test
	public void testListNucleotideExports() {
		assertArrayEquals(
			create().nucleotide().export(),
			this.manager.listNucleotide()
		);
	}

	@Test
	public void testExistsProteinExport() {
		checkProteinExists(
			TestDirectory.VALID_DATA_NAMES, 
			TestDirectory.INVALID_DATA_NAMES
		);
	}

	@Test
	public void testExistsNucleotideExport() {
		checkNucleotideExists(
			TestDirectory.VALID_DATA_NAMES, 
			TestDirectory.INVALID_DATA_NAMES
		);
	}
}
