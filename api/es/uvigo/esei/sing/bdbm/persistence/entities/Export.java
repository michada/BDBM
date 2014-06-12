package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observer;

public interface Export extends SequenceEntity, Comparable<Export> {
	public abstract File getDirectory();
	public abstract List<? extends ExportEntry> listEntries();
	public abstract ExportEntry getExportEntry(String name);
	public abstract void deleteExportEntry(ExportEntry entry)
	throws IllegalArgumentException, IOException;
	
	// Observable methods
	public void addObserver(Observer o);
	public int countObservers();
	public void deleteObserver(Observer o);
	public void deleteObservers();
	public boolean hasChanged();
	public void notifyObservers();
	public void notifyObservers(Object arg);
	
	public interface ExportEntry extends SequenceEntity {
		public Export getExport();
		public File getOutFile();
		public File[] getSequenceFiles();
		public File getSummaryFastaFile();
		public void deleteSequenceFiles();
	}
}
