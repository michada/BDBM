package es.uvigo.esei.sing.bdbm.persistence;

import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideORF;

public interface ORFRepositoryManager 
extends RepositoryManager<NucleotideORF>, NucleotideRepositoryManager<NucleotideORF, NucleotideORF> {
}
