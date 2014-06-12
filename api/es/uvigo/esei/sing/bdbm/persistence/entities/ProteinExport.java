package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.util.List;


public interface ProteinExport extends Export, ProteinSequenceEntity {
	public List<? extends ProteinExportEntry> listEntries();
	public interface ProteinExportEntry extends ExportEntry, ProteinSequenceEntity {}
}
