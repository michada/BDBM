package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultNucleotideDatabase extends AbstractDatabase implements NucleotideDatabase {
	public DefaultNucleotideDatabase(File directory) {
		super(SequenceType.NUCLEOTIDE, directory);
	}
}
