package es.uvigo.esei.sing.bdbm.fasta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FastaUtils {
	public static File[] splitFastaIntoFiles(File fastaFile) throws IOException {
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
}
