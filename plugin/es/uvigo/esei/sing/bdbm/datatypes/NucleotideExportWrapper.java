package es.uvigo.esei.sing.bdbm.datatypes;

import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSequenceEntity;

public class NucleotideExportWrapper 
extends SequenceEntityWrapper<NucleotideExport>
implements NucleotideSequenceEntity {
	public NucleotideExportWrapper(NucleotideExport export) {
		super(export);
	}
}
