package es.uvigo.esei.sing.bdbm.persistence;

import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;

public interface DatabaseRepositoryManager 
extends MixedRepositoryManager<Database, ProteinDatabase, NucleotideDatabase> {

}