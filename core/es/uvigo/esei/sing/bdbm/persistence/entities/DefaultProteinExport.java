package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class DefaultProteinExport extends AbstractExport implements ProteinExport {
	public DefaultProteinExport(File file) {
		super(SequenceType.PROTEIN, file);
	}
	
	@Override
	public List<ProteinExportEntry> listEntries() {
		final File[] files = this.getDirectory().listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
		final List<ProteinExportEntry> exportEntries = new ArrayList<ProteinExportEntry>(files.length);
		
		for (File file : files) {
			exportEntries.add(new DefaultProteinExportEntry(file));
		}
		
		return exportEntries;
	}
	
	public class DefaultProteinExportEntry extends DefaultExportEntry implements ProteinExportEntry {
		public DefaultProteinExportEntry(File baseFile) {
			super(baseFile);
		}
	}
}
