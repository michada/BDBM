package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultNucleotideFasta extends AbstractFasta implements NucleotideFasta {
	public DefaultNucleotideFasta(File file) {
		super(SequenceType.NUCLEOTIDE, file);
	}
}
