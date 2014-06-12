package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultNucleotideORF extends AbstractORF implements NucleotideORF {
	public DefaultNucleotideORF(File file) {
		super(SequenceType.NUCLEOTIDE, file);
	}
}
