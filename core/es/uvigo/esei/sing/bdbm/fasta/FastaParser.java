package es.uvigo.esei.sing.bdbm.fasta;

import java.io.File;
import java.io.IOException;

public interface FastaParser {
	public void parse(File file) throws IOException;
	public void addParseListener(FastaParserListener listener);
	public void removeParseListener(FastaParserListener listener);
	public void containsParseListener(FastaParserListener listener);
}
