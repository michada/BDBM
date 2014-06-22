package es.uvigo.esei.sing.bdbm.persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryListener;

public interface RepositoryManager<SE extends SequenceEntity> extends Repository {
	// Returns the corresponding entity. If the entity does not exists, it
	// would be created
	public abstract SE get(SequenceType sequenceType, String name)
	throws IOException;
	
	// Creates the corresponding entity. If the entity already exists, an
	// EntityAlreadyExists are thrown
	public abstract SE create(SequenceType sequenceType, String name)
	throws EntityAlreadyExistsException, IOException;
	
	// Deletes an entity, even if it is not a valid file
	public abstract boolean delete(SE entity)
	throws IOException;
	
	public void validateEntityPath(SequenceType type, File entityPath) throws EntityValidationException;
	public void validate(SE entity) throws EntityValidationException;
	
	public abstract SE[] list(SequenceType sequenceType);
	
	public abstract boolean exists(SE entity);
	public abstract boolean exists(SequenceType sequenceType, String name);
	
	public abstract void addRepositoryListener(RepositoryListener listener);
	public abstract boolean removeRepositoryListener(RepositoryListener listener);
	public abstract boolean containsRepositoryListener(RepositoryListener listener);
	public abstract List<RepositoryListener> listRepositoryListeners();
}