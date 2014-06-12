package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultNucleotideExport extends AbstractExport implements NucleotideExport {
	public DefaultNucleotideExport(File file) {
		super(SequenceType.NUCLEOTIDE, file);
	}
	
	@Override
	public List<NucleotideExportEntry> listEntries() {
		final File[] files = this.getDirectory().listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
		final List<NucleotideExportEntry> exportEntries = new ArrayList<NucleotideExportEntry>(files.length);
		
		for (File file : files) {
			exportEntries.add(new DefaultNucleotideExportEntry(file));
		}
		
		return exportEntries;
	}
	
	public class DefaultNucleotideExportEntry extends DefaultExportEntry implements NucleotideExportEntry {
		public DefaultNucleotideExportEntry(File baseFile) {
			super(baseFile);
		}
	}
}
