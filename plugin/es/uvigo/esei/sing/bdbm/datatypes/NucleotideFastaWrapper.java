package es.uvigo.esei.sing.bdbm.datatypes;

import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSequenceEntity;

public class NucleotideFastaWrapper 
extends SequenceEntityWrapper<NucleotideFasta>
implements NucleotideSequenceEntity {
	public NucleotideFastaWrapper(NucleotideFasta fasta) {
		super(fasta);
	}
}
