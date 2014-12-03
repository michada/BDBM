package es.uvigo.esei.sing.bdbm.datatypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

public class NucleotideSearchEntryWrapper 
extends AbstractSearchEntryWrapper<NucleotideSearchEntry>
implements NucleotideSearchEntry, Observer {
	public NucleotideSearchEntryWrapper(NucleotideSearchEntry searchEntry) {
		super(searchEntry);
	}
	
	@Override
	public void deleteQuery(Query query)
	throws IllegalArgumentException, IOException {
		this.getWrapped().deleteQuery(query);
	}
	
	@Override
	@ListElements(modifiable = false)
	public List<NucleotideQueryWrapper> listQueries() {
		final List<NucleotideQueryWrapper> wrappers = new ArrayList<NucleotideQueryWrapper>();

		for (NucleotideQuery query : this.getWrapped().listQueries()) {
			wrappers.add(new NucleotideQueryWrapper(query));
		}
		
		return wrappers;
	}
	
	@Override
	public NucleotideQueryWrapper getQuery(String name) {
		for (NucleotideQueryWrapper wrapper : this.listQueries()) {
			if (wrapper.getName().equals(name)) {
				return wrapper;
			}
		}
		
		return null;
	}
	
	public class NucleotideQueryWrapper
	extends SequenceEntityWrapper<NucleotideQuery>
	implements NucleotideQuery {
		public NucleotideQueryWrapper(NucleotideQuery query) {
			super(query);
		}
		
		@Override
		public SearchEntry getSearchEntry() {
			return this.getWrapped().getSearchEntry();
		}
	}
}
