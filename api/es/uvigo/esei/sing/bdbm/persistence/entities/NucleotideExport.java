package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.util.List;


public interface NucleotideExport extends Export, NucleotideSequenceEntity {
	public List<? extends NucleotideExportEntry> listEntries();
	public interface NucleotideExportEntry extends ExportEntry, NucleotideSequenceEntity {}
}
