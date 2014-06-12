package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.util.List;

public interface ProteinSearchEntry extends SearchEntry, ProteinSequenceEntity {
	@Override
	public List<? extends ProteinQuery> listQueries();
	public interface ProteinQuery extends Query, ProteinSequenceEntity {}
}
