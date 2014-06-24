package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.FileFileFilter;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultNucleotideSearchEntry
extends AbstractSearchEntry
implements NucleotideSearchEntry {
	public DefaultNucleotideSearchEntry(File file) throws IOException {
		super(SequenceType.NUCLEOTIDE, file);
	}
	
	public DefaultNucleotideSearchEntry(File file, boolean watch) throws IOException {
		super(SequenceType.NUCLEOTIDE, file, watch);
	}
	
	@Override
	public List<NucleotideQuery> listQueries() {
		final File[] files = this.getDirectory().listFiles((FileFilter) FileFileFilter.FILE);
		final List<NucleotideQuery> queries = new ArrayList<NucleotideQuery>(files.length);
		
		for (File file : files) {
			queries.add(new DefaultNucleotideQuery(file));
		}
		
		return queries;
	}
	
	@Override
	public NucleotideQuery getQuery(String name) {
		return (NucleotideQuery) super.getQuery(name);
	}
	
	public class DefaultNucleotideQuery extends DefaultQuery implements NucleotideQuery {
		public DefaultNucleotideQuery(File file) {
			super(file, DefaultNucleotideSearchEntry.this);
		}
	}
}
