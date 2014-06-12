package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.util.List;


public interface NucleotideSearchEntry extends SearchEntry, NucleotideSequenceEntity {
	@Override
	public abstract List<? extends NucleotideQuery> listQueries();
	
	public interface NucleotideQuery extends Query, NucleotideSequenceEntity {}
}
