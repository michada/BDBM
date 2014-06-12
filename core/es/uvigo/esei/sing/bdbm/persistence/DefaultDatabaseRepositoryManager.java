package es.uvigo.esei.sing.bdbm.persistence;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;

public class DefaultDatabaseRepositoryManager 
extends AbstractMixedRepositoryManager<Database, ProteinDatabase, NucleotideDatabase> 
implements DatabaseRepositoryManager {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultDatabaseRepositoryManager.class);
	
	protected File getDirectory(SequenceType sequenceType) {
		return sequenceType == SequenceType.PROTEIN ?
			this.repositoryPaths.getDBProteinsDirectory() :
			this.repositoryPaths.getDBNucleotidesDirectory();
	}
	
	@Override
	protected FileFilter getDirectoryFilter() {
		return DirectoryFileFilter.DIRECTORY;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected EntityBuilder<Database> getEntityBuilder() {
		return EntityBuilder.db();
	}

	@Override
	protected EntityValidator<Database> getEntityValidator() {
		return EntityValidator.db();
	}

	@Override
	protected boolean createEntityFiles(Database entity) {
		return entity.getDirectory().mkdirs();
	}
}
