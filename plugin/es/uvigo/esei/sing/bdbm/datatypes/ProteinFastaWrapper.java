package es.uvigo.esei.sing.bdbm.datatypes;

import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSequenceEntity;

public class ProteinFastaWrapper 
extends SequenceEntityWrapper<ProteinFasta>
implements ProteinSequenceEntity {
	public ProteinFastaWrapper(ProteinFasta fasta) {
		super(fasta);
	}
}
