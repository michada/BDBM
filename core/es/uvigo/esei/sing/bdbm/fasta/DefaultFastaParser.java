package es.uvigo.esei.sing.bdbm.fasta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultFastaParser implements FastaParser {
	protected static enum State { START, FIRST_FRAGMENT, NAME_OR_FRAGMENT };
	
	protected final List<FastaParserListener> listeners;
	
	public DefaultFastaParser() {
		this.listeners = new CopyOnWriteArrayList<>();
	}
	
	@Override
	public void parse(File file) throws FastaParseException, IOException {
		try (final BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			
			State state = State.START;
			long lineCount = 0;
			
			notifyParseStart(file);
			while ((line = br.readLine()) != null) {
				line = line.trim();
				lineCount++;
				
				if (line.isEmpty()) {
					throwInvalidFormatException(file, lineCount, "empty line");
				} else {
					switch (state) {
						case START:
							if (line.startsWith(">")) {
								notifySequenceStart(file);
								notifySequenceNameRead(file, line);
								state = State.FIRST_FRAGMENT;
							} else {
								throwInvalidFormatException(file, lineCount, "missing sequence name");
							}
							break;
						case FIRST_FRAGMENT:
							if (line.startsWith(">")) {
								throwInvalidFormatException(file, lineCount, "missing sequence");							
							} else {
								notifySequenceFragmentRead(file, line);
								state = State.NAME_OR_FRAGMENT;
							}
							break;
						case NAME_OR_FRAGMENT:
							if (line.startsWith(">")) {
								notifySequenceEnd(file);
								notifySequenceStart(file);
								notifySequenceNameRead(file, line);
								state = State.FIRST_FRAGMENT;		
							} else {
								notifySequenceFragmentRead(file, line);
							}
					}
				}
			}
			switch(state) {
			case START:
				throw new IOException(String.format("File '%s' is empty.", file.getAbsolutePath()));
			case FIRST_FRAGMENT:
				throwInvalidFormatException(file, lineCount, "last sequence is incomplete");				
			case NAME_OR_FRAGMENT:
				notifySequenceEnd(file);
			}
			
			notifyParseEnd(file);
		}
	}
	
	protected void throwInvalidFormatException(File file, long lineCount, String formatError) 
	throws FastaParseException {
		throw new FastaParseException(
			String.format("File '%s' has an invalid fasta format. Line %d is invalid: %s.", 
				file.getAbsolutePath(), lineCount, formatError
			)
		);
	}
	
	protected void notifySequenceNameRead(File file, String sequenceName) 
	throws FastaParseException {
		for (FastaParserListener listener : this.listeners) {
			listener.sequenceNameRead(file, sequenceName);
		}
	}
	
	protected void notifySequenceFragmentRead(File file, String sequence) 
	throws FastaParseException {
		for (FastaParserListener listener : this.listeners) {
			listener.sequenceFragmentRead(file, sequence);
		}
	}
	
	protected void notifyParseStart(File file) 
	throws FastaParseException {
		for (FastaParserListener listener : this.listeners) {
			listener.parseStart(file);
		}
	}
	
	protected void notifyParseEnd(File file) 
	throws FastaParseException {
		for (FastaParserListener listener : this.listeners) {
			listener.parseEnd(file);
		}
	}
	
	protected void notifySequenceStart(File file) 
	throws FastaParseException {
		for (FastaParserListener listener : this.listeners) {
			listener.sequenceStart(file);
		}
	}
	
	protected void notifySequenceEnd(File file) 
	throws FastaParseException {
		for (FastaParserListener listener : this.listeners) {
			listener.sequenceEnd(file);
		}
	}

	@Override
	public void addParseListener(FastaParserListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeParseListener(FastaParserListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void containsParseListener(FastaParserListener listener) {
		this.listeners.contains(listener);
	}
}
