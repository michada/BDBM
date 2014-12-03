package es.uvigo.esei.sing.bdbm.datatypes;

import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSequenceEntity;

public class ProteinExportWrapper 
extends SequenceEntityWrapper<ProteinExport>
implements ProteinSequenceEntity {
	public ProteinExportWrapper(ProteinExport export) {
		super(export);
	}
}
