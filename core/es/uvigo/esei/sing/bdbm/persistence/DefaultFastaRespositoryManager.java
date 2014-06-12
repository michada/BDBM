package es.uvigo.esei.sing.bdbm.persistence;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinFasta;

public class DefaultFastaRespositoryManager
extends AbstractMixedRepositoryManager<Fasta, ProteinFasta, NucleotideFasta> 
implements FastaRepositoryManager {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultFastaRespositoryManager.class);

	@Override
	protected File getDirectory(SequenceType sequenceType) {
		return sequenceType == SequenceType.PROTEIN ?
			this.repositoryPaths.getFastaProteinsDirectory() :
			this.repositoryPaths.getFastaNucleotidesDirectory();
	}
	
	@Override
	protected FileFilter getDirectoryFilter() {
		return FileFileFilter.FILE;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected EntityBuilder<Fasta> getEntityBuilder() {
		return EntityBuilder.fasta();
	}

	@Override
	protected EntityValidator<Fasta> getEntityValidator() {
		return EntityValidator.fasta();
	}

	@Override
	protected boolean createEntityFiles(Fasta entity) {
		final File parentFile = entity.getBaseFile().getParentFile();
		return parentFile.isDirectory() || parentFile.mkdirs();
	}
}
