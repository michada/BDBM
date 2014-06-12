package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

public interface ORF extends SequenceEntity, Comparable<ORF> {
	public abstract File getFile();
}
