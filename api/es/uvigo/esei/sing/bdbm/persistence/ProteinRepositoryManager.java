package es.uvigo.esei.sing.bdbm.persistence;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSequenceEntity;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public interface ProteinRepositoryManager<SE extends SequenceEntity, P extends ProteinSequenceEntity> 
extends RepositoryManager<SE> {
	public abstract P getProtein(String name) throws IOException;
	public abstract P createProtein(String name)
	throws EntityAlreadyExistsException, IOException;
	public abstract P[] listProtein();
	public abstract boolean existsProtein(String name);
}
