package es.uvigo.esei.sing.bdbm.persistence;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinExport;

public class DefaultExportRepositoryManager
extends AbstractMixedRepositoryManager<Export, ProteinExport, NucleotideExport>
implements ExportRepositoryManager {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultExportRepositoryManager.class);

	@Override
	protected File getDirectory(SequenceType sequenceType) {
		return sequenceType == SequenceType.PROTEIN ?
			this.repositoryPaths.getExportProteinsDirectory() :
			this.repositoryPaths.getExportNucleotidesDirectory();
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
	protected EntityBuilder<Export> getEntityBuilder() {
		return EntityBuilder.export();
	}

	@Override
	protected EntityValidator<Export> getEntityValidator() {
		return EntityValidator.export();
	}

	@Override
	protected boolean createEntityFiles(Export entity) {
		return entity.getDirectory().mkdirs();
	}
}
