package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.util.List;
import java.util.Observer;

public interface Export extends SequenceEntity, Comparable<Export> {
	public abstract File getDirectory();
	public abstract List<? extends ExportEntry> listEntries();
	public abstract ExportEntry getExportEntry(String name);
	
	// Observable methods
	public void addObserver(Observer o);
	public int countObservers();
	public void deleteObserver(Observer o);
	public void deleteObservers();
	public boolean hasChanged();
	public void notifyObservers();
	public void notifyObservers(Object arg);
	
	public interface ExportEntry extends SequenceEntity {
		public File getOutFile();
		public File[] getSequenceFiles();
		public File getSummaryFastaFile();
		public void deleteSequenceFiles();
	}
}
