package es.uvigo.esei.sing.bdbm.persistence;

import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

public interface SearchEntryRepositoryManager
extends MixedRepositoryManager<SearchEntry, ProteinSearchEntry, NucleotideSearchEntry> {
}
