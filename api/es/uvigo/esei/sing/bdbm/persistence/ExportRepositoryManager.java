package es.uvigo.esei.sing.bdbm.persistence;

import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinExport;

public interface ExportRepositoryManager
extends MixedRepositoryManager<Export, ProteinExport, NucleotideExport> {
}
