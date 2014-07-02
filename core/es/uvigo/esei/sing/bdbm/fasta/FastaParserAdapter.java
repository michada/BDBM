package es.uvigo.esei.sing.bdbm.fasta;

import java.io.File;

public class FastaParserAdapter implements FastaParserListener {
	@Override
	public void parseStart(File file) {}

	@Override
	public void sequenceStart(File file) {}

	@Override
	public void sequenceNameRead(File file, String sequenceName) {}

	@Override
	public void sequenceFragmentRead(File file, String sequence) {}

	@Override
	public void sequenceEnd(File file) {}

	@Override
	public void parseEnd(File file) {}
}
