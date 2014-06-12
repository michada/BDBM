package es.uvigo.esei.sing.bdbm.persistence.watcher;

import java.io.File;

import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public class RepositoryEvent {
	public enum Type {
		CREATE,
		DELETE,
		MODIFY,
		INVALIDATED
	};
	
	private final SequenceEntity entity;
	private final File modifiedFile;
	private final Type type;
	
	public RepositoryEvent(SequenceEntity entity, File modifiedFile) {
		this.entity = entity;
		this.modifiedFile = modifiedFile;
		this.type = Type.MODIFY;
	}
	
	public RepositoryEvent(SequenceEntity entity, Type type) {
		this.entity = entity;
		this.modifiedFile = null;
		this.type = type;
	}
	
	public SequenceEntity getEntity() {
		return entity;
	}
	
	public File getModifiedFile() {
		return modifiedFile;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return this.getType().name() + ": " + this.getEntity();
	}
}
