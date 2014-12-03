package es.uvigo.esei.sing.bdbm.datatypes;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

@Datatype(
	namingMethod = "getName",
	renameable = false,
	removable = false,
	structure = Structure.LIST
)
public abstract class AbstractSearchEntryWrapper<T extends SearchEntry> 
extends SequenceEntityWrapper<T> 
implements SearchEntry, Observer {
	public AbstractSearchEntryWrapper(T wrappedEntity) {
		super(wrappedEntity);
		
		wrappedEntity.addObserver(this);
	}

	@Override
	public File getDirectory() {
		return this.getWrapped().getDirectory();
	}

//	@Override
//	@ListElements(modifiable = false)
//	public abstract List<? extends Query> listQueries(); /*{
//		final List<Query> wrappers = new ArrayList<Query>();
//
//		for (Query query : this.getWrapped().listQueries()) {
//			wrappers.add(new QueryWrapper(query));
//		}
//		
//		return wrappers;
//	}*/

	@Override
	public int compareTo(SearchEntry o) {
		return this.getWrapped().compareTo(o);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers();
	}

	protected static class QueryWrapper 
	extends SequenceEntityWrapper<Query> 
	implements Query {
		public QueryWrapper(Query wrappedEntity) {
			super(wrappedEntity);
		}
		
		@Override
		public SearchEntry getSearchEntry() {
			return this.getWrapped().getSearchEntry();
		}
	}
	
	public static Query unwrapToBase(QueryWrapper wrapper) {
		return wrapper.getWrapped();
	}
}