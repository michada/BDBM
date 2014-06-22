package es.uvigo.esei.sing.bdbm.persistence;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

public class DefaultSearchEntryRepositoryManager
extends AbstractMixedRepositoryManager<SearchEntry, ProteinSearchEntry, NucleotideSearchEntry>
implements SearchEntryRepositoryManager {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultSearchEntryRepositoryManager.class);

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected EntityBuilder<SearchEntry> getEntityBuilder() {
		return EntityBuilder.searchEntry();
	}

	@Override
	protected EntityValidator<SearchEntry> getEntityValidator() {
		return EntityValidator.searchEntry();
	}

	@Override
	protected FileFilter getDirectoryFilter() {
		return DirectoryFileFilter.DIRECTORY;
	}
	
	@Override
	protected File getDirectory(SequenceType sequenceType) {
		return sequenceType == SequenceType.PROTEIN ?
			this.repositoryPaths.getSearchEntryProteinsDirectory() :
			this.repositoryPaths.getSearchEntryNucleotidesDirectory();
	}

	@Override
	protected boolean createEntityFiles(SearchEntry entity) {
		return entity.getDirectory().mkdirs();
	}
	
	@Override
	public void validateEntityPath(SequenceType type, File entityPath) throws EntityValidationException {
		final EntityValidator<SearchEntry> entityValidator = this.getEntityValidator();
		try {
			entityValidator.validate(EntityBuilder.createUnwatchedSearchEntry(type, entityPath));
		} catch (IOException e) {
			throw new EntityValidationException("Error validating search entry", e, null);
		}
	}
}
