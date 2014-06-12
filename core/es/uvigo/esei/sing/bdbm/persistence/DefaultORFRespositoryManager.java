package es.uvigo.esei.sing.bdbm.persistence;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideORF;

public class DefaultORFRespositoryManager
extends AbstractRepositoryManager<NucleotideORF> 
implements ORFRepositoryManager {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultORFRespositoryManager.class);

	@Override
	protected File getDirectory() {
		return this.repositoryPaths.getORFNucleotidesDirectory();
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
	protected EntityBuilder<NucleotideORF> getEntityBuilder() {
		return EntityBuilder.orf();
	}

	@Override
	protected EntityValidator<NucleotideORF> getEntityValidator() {
		return EntityValidator.orf();
	}

	@Override
	protected boolean createEntityFiles(NucleotideORF entity) {
		final File parentFile = entity.getBaseFile().getParentFile();
		return parentFile.isDirectory() || parentFile.mkdirs();
	}

	@Override
	protected SequenceType getSequenceType() {
		return SequenceType.NUCLEOTIDE;
	}

	@Override
	public NucleotideORF getNucleotide(String name) throws IOException {
		return this.get(name);
	}

	@Override
	public NucleotideORF createNucleotide(String name)
	throws EntityAlreadyExistsException, IOException {
		return this.create(name);
	}

	@Override
	public NucleotideORF[] listNucleotide() {
		return this.list();
	}

	@Override
	public boolean existsNucleotide(String name) {
		return this.exists(name);
	}
}
