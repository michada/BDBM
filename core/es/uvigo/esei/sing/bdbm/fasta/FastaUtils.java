package es.uvigo.esei.sing.bdbm.fasta;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.uvigo.esei.sing.bdbm.fasta.naming.NameSummarizerFactory;

public class FastaUtils {
	public static enum RenameMode {
		NONE, SMART, GENERIC, PREFIX;
	}

	public static File[] splitFastaIntoFiles(File fastaFile) throws IOException, FastaParseException {
		final List<File> files = new ArrayList<>();
		
		final FastaParser parser = new DefaultFastaParser();
		parser.addParseListener(new FastaParserAdapter() {
			private String name = null;
			private StringBuilder sequence = new StringBuilder();
			
			@Override
			public void sequenceNameRead(File file, String sequenceName) {
				this.name = sequenceName;
			}
			
			@Override
			public void sequenceFragmentRead(File file, String sequence) {
				this.sequence.append(sequence);
			}
			
			@Override
			public void sequenceEnd(File file) {
				try {
					final Path tmpFilePath = Files.createTempFile("bdbm", "fasta");
					
					final String content = name + '\n' + sequence;
					
					Files.write(tmpFilePath, content.getBytes());
					
					final File tmpFile = tmpFilePath.toFile();
					tmpFile.deleteOnExit();
					
					files.add(tmpFile);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		parser.parse(fastaFile);
		
		if (files.size() == 1) {
			files.get(0).delete();
			return new File[] { fastaFile };
		} else {
			return files.toArray(new File[files.size()]);
		}
	}
	
	public static void fastaSequenceRenaming(RenameMode mode, File fastaFile, PrintWriter writer, Object additionalParameters)
	throws FastaParseException, IOException {
		fastaSequenceRenaming(mode, fastaFile, 0, writer, additionalParameters);
	}
	
	public static void fastaSequenceRenaming(RenameMode mode, File fastaFile, int fragmentLength, PrintWriter writer, Object additionalParameters)
	throws FastaParseException, IOException {
		switch(mode) {
		case GENERIC:
			if (additionalParameters instanceof int[]) {
				genericFastaSequenceRenaming(fastaFile, (int[]) additionalParameters, fragmentLength, writer);
			} else {
				throw new IllegalArgumentException("Generic renaming requires a int[] with the indexes as additional parameter");
			}
			break;
		case PREFIX:
			if (additionalParameters instanceof String) {
				prefixFastaSequenceRenaming(fastaFile, (String) additionalParameters, fragmentLength, writer);
			} else {
				throw new IllegalArgumentException("Prefix renaming requires a String with the prefix as additional parameter");
			}
			break;
		case SMART:
			smartFastaSequenceRenaming(fastaFile, fragmentLength, writer);
			break;
		case NONE:
			changeSequenceLength(fastaFile, fragmentLength, writer);
			break;
		}
	}
	
	public static void prefixFastaSequenceRenaming(File fastaFile, final String prefix, final PrintWriter writer)
	throws FastaParseException, IOException {
		prefixFastaSequenceRenaming(fastaFile, prefix, 0, writer);
	}
	
	public static void prefixFastaSequenceRenaming(File fastaFile, final String prefix, final int fragmentLength, final PrintWriter writer)
	throws FastaParseException, IOException {
		final FastaParser parser = new DefaultFastaParser();
		
		if (fragmentLength == 0) {
			parser.addParseListener(new FastaParserAdapter() {
				int prefixCounter = 1;
				
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					writer.println(">" + prefix + prefixCounter++ + "_" + sequenceName);
				}
				
				@Override
				public void sequenceFragmentRead(File file, String sequenceFragment) {
					writer.println(sequenceFragment);
				}
			});
		} else {
			parser.addParseListener(new SequenceResizingFastaParserListener(fragmentLength) {
				int prefixCounter = 1;
				
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					writer.println(">" + prefix + prefixCounter++ + "_" + sequenceName);
				}
				
				@Override
				public void sequenceEnd(File file, String resizedSequence) {
					writer.println(resizedSequence);
					
				}
			});
		}
		
		parser.parse(fastaFile);
	}
	
	public static void smartFastaSequenceRenaming(File fastaFile, final PrintWriter writer)
	throws FastaParseException, IOException {
		smartFastaSequenceRenaming(fastaFile, 0, writer);
	}
	
	public static void smartFastaSequenceRenaming(File fastaFile, final int fragmentLength, final PrintWriter writer)
	throws FastaParseException, IOException {
		final FastaParser parser = new DefaultFastaParser();

		if (fragmentLength == 0) {
			parser.addParseListener(new FastaParserAdapter() {
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					writer.println(NameSummarizerFactory.createNameSummarizer(sequenceName).summarize(sequenceName));
				}
				
				@Override
				public void sequenceFragmentRead(File file, String sequenceFragment) {
					writer.println(sequenceFragment);
				}
			});
		} else {
			parser.addParseListener(new SequenceResizingFastaParserListener(fragmentLength) {
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					writer.println(NameSummarizerFactory.createNameSummarizer(sequenceName).summarize(sequenceName));
				}
				
				@Override
				public void sequenceEnd(File file, String resizedSequence) {
					writer.println(resizedSequence);
				}
			});
		}
		
		parser.parse(fastaFile);
	}
	
	public static void genericFastaSequenceRenaming(File fastaFile, int[] indexes, final PrintWriter writer)
	throws FastaParseException, IOException {
		genericFastaSequenceRenaming(fastaFile, indexes, 0, writer);
	}
	
	public static void genericFastaSequenceRenaming(File fastaFile, final int[] indexes, final int fragmentLength, final PrintWriter writer)
	throws FastaParseException, IOException {
		final FastaParser parser = new DefaultFastaParser();
		
		if (fragmentLength == 0) {
			parser.addParseListener(new FastaParserAdapter() {
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					writer.println(NameSummarizerFactory.createGenericNameSummarizer(indexes).summarize(sequenceName));
				}
				
				@Override
				public void sequenceFragmentRead(File file, String sequenceFragment) {
					writer.println(sequenceFragment);
				}
			});
		} else {
			parser.addParseListener(new SequenceResizingFastaParserListener(fragmentLength) {
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					writer.println(NameSummarizerFactory.createGenericNameSummarizer(indexes).summarize(sequenceName));
				}
				
				@Override
				public void sequenceEnd(File file, String resizedSequence) {
					writer.println(resizedSequence);
				}
			});
		}
		
		parser.parse(fastaFile);
	}
	
	@SuppressWarnings("unchecked")
	public static void mergeFastas(final File[] fastaFiles, File outputFile) throws FastaParseException, IOException {
		final Map<String, List<int[]>[]> indexes = new LinkedHashMap<>();
		
		final RandomAccessFile[] rafs = new RandomAccessFile[fastaFiles.length];
		try {
			int i = 0;
			for (File fastaFile : fastaFiles) {
				final Map<String, List<int[]>> fastaIndexes = indexFastaFile(fastaFiles[i]);
				
				for (Map.Entry<String, List<int[]>> seqIndex : fastaIndexes.entrySet()) {
					if (!indexes.containsKey(seqIndex.getKey()))
						indexes.put(seqIndex.getKey(), (List<int[]>[]) new List[fastaFiles.length]);
					
					indexes.get(seqIndex.getKey())[i] = seqIndex.getValue();
				}
				
				rafs[i++] = new RandomAccessFile(fastaFile, "r");
			}
			
			try (PrintWriter pw = new PrintWriter(outputFile)) {
				for (Map.Entry<String, List<int[]>[]> entry : indexes.entrySet()) {
					pw.println(entry.getKey());
					
					final StringBuilder sb = new StringBuilder();
					for (int j = 0; j < rafs.length; j++) {
						final List<int[]> fastaIndexesList = entry.getValue()[j];
						
						for (int[] fastaIndexes : fastaIndexesList) {
							if (fastaIndexes[0] != fastaIndexes[1]) {
								final byte[] data = new byte[fastaIndexes[1] - fastaIndexes[0]];
								rafs[j].seek(fastaIndexes[0]);
								rafs[j].read(data);
								
								sb.append(new String(data));
							}
						}
					}
					
					pw.println(sb.toString().replaceAll("[\n\r]", ""));
				}
			}
		} finally {
			for (RandomAccessFile raf : rafs) {
				if (raf != null)
					try { raf.close(); }
					catch (IOException ioe) {}
			}
		}
	}
	
	public static Map<String, List<int[]>> indexFastaFile(File fasta) throws FastaParseException, IOException {
		final Map<String, List<int[]>> indexes = new LinkedHashMap<>();
		final int newLineSize = newLineSize(fasta);
		
		final FastaParser parser = new DefaultFastaParser();
		parser.addParseListener(new FastaParserAdapter() {
			private int index;
			private int[] currentIndexes;
			
			@Override
			public void parseStart(File file) throws FastaParseException {
				this.index = 0;
				this.currentIndexes = null;
			}
			
			@Override
			public void sequenceNameRead(File file, String sequenceName)
			throws FastaParseException {
				if (!indexes.containsKey(sequenceName))
					indexes.put(sequenceName, new LinkedList<int[]>());
				indexes.get(sequenceName).add(this.currentIndexes = new int[2]);
				
				this.index += sequenceName.getBytes().length;
				this.currentIndexes[0] = this.index + newLineSize;
			}
			
			@Override
			public void sequenceFragmentRead(File file, String sequence)
			throws FastaParseException {
				this.index += newLineSize + sequence.getBytes().length;
			}
			
			@Override
			public void sequenceEnd(File file) throws FastaParseException {
				this.currentIndexes[1] = this.index;
				
				this.index += newLineSize;
				this.currentIndexes = null;
			}
		});
		
		parser.parse(fasta);
		
		return indexes;
	}
	
	public static void changeSequenceLength(File fasta, final int fragmentLength, final PrintWriter pw)
	throws IOException, FastaParseException {
		final DefaultFastaParser parser = new DefaultFastaParser();
		parser.addParseListener(new SequenceResizingFastaParserListener(fragmentLength) {
			@Override
			public void sequenceNameRead(File file, String sequenceName) {
				pw.println(sequenceName);
			}
			
			@Override
			public void sequenceEnd(File file, String resizedSequence) {
				pw.println(resizedSequence);
			}
		});
		
		parser.parse(fasta);
	}
	
	public static String resizeSequence(String sequence, int fragmentLength) {
		if (fragmentLength < 0) {
			return sequence;
		} else {
			sequence = sequence.replaceAll("[\n\r]", "");
			
			if (fragmentLength == 0 || fragmentLength > sequence.length()) {
				return sequence;
			} else {
				final StringBuilder sb = new StringBuilder();
				final String nl = System.getProperty("line.separator");
				
				for (int i = 0; i < sequence.length(); i += fragmentLength) {
					final int endIndex = Math.min(sequence.length(), i + fragmentLength);
					
					sb.append(sequence.substring(i, endIndex)).append(nl);
				}
				
				// Last new line deletion
				sb.delete(sb.length() - nl.length(), sb.length());
				
				return sb.toString();
			}
		}
	}
	
	private static int newLineSize(File file) throws IOException {
		int value;
		
		try (final Reader reader = new FileReader(file)) {
			while ((value = reader.read()) != -1) {
				final char chr = (char) value;
				
				if (chr == '\n') {
					return 1;
				} else if (chr == '\r') {
					value = reader.read();
					
					if (value != -1 && (char) value == '\n') {
						return 2;
					} else {
						return -1;
					}
				}
			}
		}
		
		return -1;
	}
	
	public static abstract class SequenceResizingFastaParserListener extends FastaParserAdapter {
		protected final int fragmentLength;
		protected StringBuilder sequence;
		
		public SequenceResizingFastaParserListener(int fragmentLength) {
			this.fragmentLength = fragmentLength;
		}
		
		@Override
		public void sequenceStart(File file) throws FastaParseException {
			this.sequence = new StringBuilder();
		}
		
		@Override
		public void sequenceFragmentRead(File file, String fragment) {
			this.sequence.append(fragment);
		}
		
		@Override
		public void sequenceEnd(File file) {
			this.sequenceEnd(file, resizeSequence(sequence.toString(), this.fragmentLength));
			this.sequence = null;
		}
		
		public abstract void sequenceEnd(File file, String resizedSequence);
	}
}
