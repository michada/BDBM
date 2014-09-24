package es.uvigo.esei.sing.bdbm.fasta;

import java.io.File;

public class FastaParserAdapter implements FastaParserListener {
	@Override
	public void parseStart(File file) throws FastaParseException {}

	@Override
	public void sequenceStart(File file) throws FastaParseException {}

	@Override
	public void sequenceNameRead(File file, String sequenceName) throws FastaParseException {}

	@Override
	public void sequenceFragmentRead(File file, String sequence) throws FastaParseException {}

	@Override
	public void sequenceEnd(File file) throws FastaParseException {}

	@Override
	public void parseEnd(File file) throws FastaParseException {}
}
