package es.uvigo.esei.sing.bdbm.persistence;

import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinFasta;

public interface FastaRepositoryManager 
extends MixedRepositoryManager<Fasta, ProteinFasta, NucleotideFasta> {
}
