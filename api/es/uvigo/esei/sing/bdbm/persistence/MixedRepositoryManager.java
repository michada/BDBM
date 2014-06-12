package es.uvigo.esei.sing.bdbm.persistence;

import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSequenceEntity;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSequenceEntity;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;


public interface MixedRepositoryManager<
	SE extends SequenceEntity, 
	P extends ProteinSequenceEntity, 
	N extends NucleotideSequenceEntity
> extends ProteinRepositoryManager<SE, P>, NucleotideRepositoryManager<SE, N> {
}