package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public interface SequenceEntity {
	public abstract File getBaseFile();
	public abstract SequenceType getType();
	public abstract String getName();
}