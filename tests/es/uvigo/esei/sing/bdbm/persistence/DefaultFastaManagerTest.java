package es.uvigo.esei.sing.bdbm.persistence;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.TestDirectory;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public class DefaultFastaManagerTest extends AbstractRepositoryManagerTest<DefaultFastaRespositoryManager> {
	@Override
	protected DefaultFastaRespositoryManager newManager() {
		return new DefaultFastaRespositoryManager();
	}
	
	@Override
	protected void expectations(RepositoryPaths pathsMock) {
		expect(this.pathsMock.getFastaNucleotidesDirectory())
			.andReturn(TestDirectory.NUCLEOTIDE_FASTA_DIRECTORY)
			.anyTimes();
		expect(this.pathsMock.getFastaProteinsDirectory())
			.andReturn(TestDirectory.PROTEIN_FASTA_DIRECTORY)
			.anyTimes();
	}
	
	@Test
	@Ignore("Not yet implemented")
	public void testCreateFasta() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testListFastas() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExistsFasta() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testListProteinFasta() {
		assertArrayEquals(
			create().protein().fasta(),
			this.manager.listProtein()
		);
	}

	@Test
	public void testListNucleotideFasta() {
		assertArrayEquals(
			create().nucleotide().fasta(),
			this.manager.listNucleotide()
		);
	}

	@Test
	public void testExistsProteinFasta() {
		checkProteinExists(
			validDataNames(SequenceType.PROTEIN), 
			invalidDataNames(SequenceType.PROTEIN)
		);
	}

	@Test
	public void testExistsNucleotideFasta() {
		checkNucleotideExists(
			validDataNames(SequenceType.NUCLEOTIDE), 
			invalidDataNames(SequenceType.NUCLEOTIDE)
		);
	}
	
	protected static String[] validDataNames(SequenceType seqType) {
		final String[] dataNames = new String[TestDirectory.VALID_DATA_NAMES.length];
		
		for (int i = 0; i < dataNames.length; i++) {
			dataNames[i] = TestDirectory.VALID_DATA_NAMES[i] + "." + seqType.getDBType() + ".fasta";
		}
		
		return dataNames;
	}
	
	protected static String[] invalidDataNames(SequenceType seqType) {
		final String[] dataNames = new String[TestDirectory.INVALID_DATA_NAMES.length];
		
		for (int i = 0; i < dataNames.length; i++) {
			dataNames[i] = TestDirectory.INVALID_DATA_NAMES[i] + "." + seqType.getDBType() + ".fasta";
		}
		
		return dataNames;
	}
}
