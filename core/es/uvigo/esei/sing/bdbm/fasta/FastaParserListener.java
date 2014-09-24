package es.uvigo.esei.sing.bdbm.fasta;

import java.io.File;

public interface FastaParserListener {
	public abstract void parseStart(File file) throws FastaParseException;
	public abstract void sequenceStart(File file) throws FastaParseException;
	public abstract void sequenceNameRead(File file, String sequenceName) throws FastaParseException;
	public abstract void sequenceFragmentRead(File file, String sequenceFragment) throws FastaParseException;
	public abstract void sequenceEnd(File file) throws FastaParseException;
	public abstract void parseEnd(File file) throws FastaParseException;
}
