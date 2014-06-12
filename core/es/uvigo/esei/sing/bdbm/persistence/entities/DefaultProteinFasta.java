package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultProteinFasta extends AbstractFasta implements ProteinFasta {
	public DefaultProteinFasta(File file) {
		super(SequenceType.PROTEIN, file);
	}
}
