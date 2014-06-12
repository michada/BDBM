package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultProteinDatabase extends AbstractDatabase implements ProteinDatabase {
	public DefaultProteinDatabase(File directory) {
		super(SequenceType.PROTEIN, directory);
	}
}
