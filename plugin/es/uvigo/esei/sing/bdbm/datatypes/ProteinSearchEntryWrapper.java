package es.uvigo.esei.sing.bdbm.datatypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

public class ProteinSearchEntryWrapper 
extends AbstractSearchEntryWrapper<ProteinSearchEntry>
implements ProteinSearchEntry, Observer {
	public ProteinSearchEntryWrapper(ProteinSearchEntry searchEntry) {
		super(searchEntry);
	}
	
	@Override
	public void deleteQuery(Query query)
	throws IllegalArgumentException, IOException {
		this.getWrapped().deleteQuery(query);
	}
	
	@Override
	@ListElements(modifiable = false)
	public List<ProteinQueryWrapper> listQueries() {
		final List<ProteinQueryWrapper> wrappers = new ArrayList<ProteinQueryWrapper>();

		for (ProteinQuery query : this.getWrapped().listQueries()) {
			wrappers.add(new ProteinQueryWrapper(query));
		}
		
		return wrappers;
	}
	
	@Override
	public ProteinQueryWrapper getQuery(String name) {
		for (ProteinQueryWrapper wrapper : this.listQueries()) {
			if (wrapper.getName().equals(name)) {
				return wrapper;
			}
		}
		
		return null;
	}
	
	public class ProteinQueryWrapper
	extends SequenceEntityWrapper<ProteinQuery>
	implements ProteinQuery {
		public ProteinQueryWrapper(ProteinQuery query) {
			super(query);
		}
		
		@Override
		public SearchEntry getSearchEntry() {
			return this.getWrapped().getSearchEntry();
		}
	}
}
