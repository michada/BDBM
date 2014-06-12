package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

public interface Fasta extends SequenceEntity, Comparable<Fasta> {
	public abstract File getFile();
}
