package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.util.Observable;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class AbstractSequenceEntity extends Observable implements SequenceEntity {
	protected final SequenceType type;
	protected final File baseFile;

	public AbstractSequenceEntity(SequenceType type, File baseFile) {
		this.type = type;
		this.baseFile = baseFile;
	}
	
	@Override
	public File getBaseFile() {
		return this.baseFile;
	}

	@Override
	public SequenceType getType() {
		return type;
	}

	@Override
	public String getName() {
		return this.baseFile.getName();
	}

	@Override
	public String toString() {
		return this.getName();
//		return new StringBuilder(this.getName())
//			.append(" [").append(this.getType()).append(']')
//		.toString();
	}
}