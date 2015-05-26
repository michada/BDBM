package es.uvigo.esei.sing.bdbm.persistence;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSequenceEntity;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public interface NucleotideRepositoryManager<SE extends SequenceEntity, N extends NucleotideSequenceEntity> 
extends RepositoryManager<SE> {
	public abstract N getNucleotide(String name) throws IOException;
	public abstract N createNucleotide(String name)
	throws EntityAlreadyExistsException, IOException;
	public abstract N[] listNucleotide();
	public abstract boolean existsNucleotide(String name);
}
