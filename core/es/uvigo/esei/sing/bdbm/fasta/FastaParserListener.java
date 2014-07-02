package es.uvigo.esei.sing.bdbm.fasta;

import java.io.File;

public interface FastaParserListener {
	public abstract void parseStart(File file);
	public abstract void sequenceStart(File file);
	public abstract void sequenceNameRead(File file, String sequenceName);
	public abstract void sequenceFragmentRead(File file, String sequence);
	public abstract void sequenceEnd(File file);
	public abstract void parseEnd(File file);
}
