package es.uvigo.esei.sing.bdbm.persistence;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;

import org.junit.After;
import org.junit.Before;

import es.uvigo.esei.sing.bdbm.TestDirectory;
import es.uvigo.esei.sing.bdbm.TestDirectory.DirectoryType;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public abstract class AbstractRepositoryManagerTest<T extends MixedRepositoryManager<?, ?, ?>> extends TestDirectoryTest {
	protected T manager;
	protected RepositoryPaths pathsMock;
	
	protected abstract void expectations(RepositoryPaths pathsMock);
	
	@Before
	public void setUp() throws Exception {
		this.pathsMock = createMock("repositoryPaths", RepositoryPaths.class);
		
		this.expectations(this.pathsMock);

		replay(this.pathsMock);
		
		this.manager = this.newManager();
		
		this.manager.setRepositoryPaths(this.pathsMock);
	}

	@After
	public void tearDown() throws Exception {
		verify(this.pathsMock);
		
		this.manager = null;
		this.pathsMock = null;
	}
	
	protected abstract T newManager();
	
	protected void checkProteinExists(final String[] validDataNames, final String[] invalidDataNames) {
		for (String dataName : validDataNames) {
			assertTrue("Valid protein does not exists: " + dataName, this.manager.existsProtein(dataName));
		}
		
		for (String invalidDataName : invalidDataNames) {
			assertFalse("Invalid protein exists: " + invalidDataName, this.manager.existsProtein(invalidDataName));
		}
	}

	protected void checkNucleotideExists(final String[] validDataNames, final String[] invalidDataNames) {
		for (String dataName : validDataNames) {
			assertTrue("Valid nucleotide does not exists: " + dataName, this.manager.existsNucleotide(dataName));
		}
		
		for (String invalidDataName : invalidDataNames) {
			assertFalse("Invalid nucleotide exists: " + invalidDataName, this.manager.existsNucleotide(invalidDataName));
		}
	}

	protected static Create create() {
		return new Create();
	}
	
	protected final static class Create {
		public CreateSequence protein() {
			return new CreateSequence(SequenceType.PROTEIN);
		}
		
		public CreateSequence nucleotide() {
			return new CreateSequence(SequenceType.NUCLEOTIDE);
		}
	}
	
	protected final static class CreateSequence {
		private final SequenceType sequenceType;

		public CreateSequence(SequenceType sequenceType) {
			this.sequenceType = sequenceType;
		}
		
		public Database[] db() {
			return CreateSequence.createEntities(
				EntityBuilder.db(), this.sequenceType, DirectoryType.DB
			);
		}
		
		public SearchEntry[] searchEntry() {
			return CreateSequence.createEntities(
				EntityBuilder.searchEntry(), this.sequenceType, DirectoryType.SEARCH_ENTRY
			);
		}
		
		public Export[] export() {
			return CreateSequence.createEntities(
				EntityBuilder.export(), this.sequenceType, DirectoryType.EXPORT
			);
		}
		
		public Fasta[] fasta() {
			return CreateSequence.createEntities(
				EntityBuilder.fasta(), this.sequenceType, DirectoryType.FASTA
			);
		}

		@SuppressWarnings("unchecked")
		private final static <T extends SequenceEntity> T[] createEntities(
			EntityBuilder<T> builder, SequenceType seqType, DirectoryType dirType
		) {
			final Class<T> entityClass = 
				(Class<T>) ((ParameterizedType) builder.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			
			final File[] files = TestDirectory.list().valid().sequence(seqType).directory(dirType);
			
			final T[] entities = (T[]) Array.newInstance(entityClass, files.length);
			for (int i = 0; i < files.length; i++) {
				entities[i] = builder.create(seqType, files[i]);
			}
			
			return entities;
		}
	}
}
