package es.uvigo.esei.sing.bdbm.datatypes;

import java.io.File;
import java.util.List;

import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;

public class ProteinDatabaseWrapper 
extends SequenceEntityWrapper<ProteinDatabase>
implements ProteinDatabase {
	public ProteinDatabaseWrapper(ProteinDatabase database) {
		super(database);
	}
	
	@Override
	public File getDirectory() {
		return this.getWrapped().getDirectory();
	}
	
	@Override
	public boolean isAggregated() {
		return this.getWrapped().isAggregated();
	}
	
	@Override
	public boolean isRegular() {
		return this.getWrapped().isRegular();
	}

	@Override
	public List<String> listAccessions() {
		return this.getWrapped().listAccessions();
	}

	@Override
	public int compareTo(Database o) {
		return this.getWrapped().compareTo(o);
	}
}
