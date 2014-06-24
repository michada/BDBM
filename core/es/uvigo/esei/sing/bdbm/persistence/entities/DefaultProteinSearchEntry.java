package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.FileFileFilter;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultProteinSearchEntry 
extends AbstractSearchEntry 
implements ProteinSearchEntry {
	public DefaultProteinSearchEntry(File file) throws IOException {
		super(SequenceType.PROTEIN, file);
	}
	
	public DefaultProteinSearchEntry(File file, boolean watch) throws IOException {
		super(SequenceType.PROTEIN, file, watch);
	}
	
	@Override
	public List<ProteinQuery> listQueries() {
		final File[] files = this.getDirectory().listFiles((FileFilter) FileFileFilter.FILE);
		final List<ProteinQuery> queries = new ArrayList<ProteinQuery>(files.length);
		
		for (File file : files) {
			queries.add(new DefaultProteinQuery(file));
		}
		
		return queries;
	}
	
	@Override
	public ProteinQuery getQuery(String name) {
		return (ProteinQuery) super.getQuery(name);
	}
	
	public class DefaultProteinQuery extends DefaultQuery implements ProteinQuery {
		public DefaultProteinQuery(File file) {
			super(file, DefaultProteinSearchEntry.this);
		}
	}
}
