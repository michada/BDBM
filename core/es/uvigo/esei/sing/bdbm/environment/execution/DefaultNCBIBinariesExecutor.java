package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;
import es.uvigo.esei.sing.bdbm.fasta.DefaultFastaParser;
import es.uvigo.esei.sing.bdbm.fasta.FastaParseException;
import es.uvigo.esei.sing.bdbm.fasta.FastaParser;
import es.uvigo.esei.sing.bdbm.fasta.FastaParserAdapter;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

public class DefaultNCBIBinariesExecutor
extends AbstractBinariesExecutor<NCBIBinaries>
implements NCBIBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultNCBIBinariesExecutor.class);
	
	public DefaultNCBIBinariesExecutor() {}

	public DefaultNCBIBinariesExecutor(NCBIBinaries nBinaries)
	throws BinaryCheckException {
		this.setBinaries(nBinaries);
	}

	@Override
	public void setBinaries(
		NCBIBinaries nBinaries
	) throws BinaryCheckException {
		DefaultNCBIBinariesChecker.checkAll(nBinaries);
		
		this.binaries = nBinaries;
	}
	
	@Override
	public boolean checkNCBIBinaries(NCBIBinaries bBinaries) {
		try {
			DefaultNCBIBinariesChecker.checkAll(bBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}
	
	@Override
	public ExecutionResult splignCompart(
		NucleotideFasta genomeFasta, 
		NucleotideDatabase genomeDB, 
		NucleotideFasta cdsFasta,
		NucleotideDatabase cdsDB,
		NucleotideFasta fasta
	) throws InterruptedException, ExecutionException, IOException, FastaParseException {
		try (final DirectoryManager dirManager = new DirectoryManager(
			genomeFasta, genomeDB, cdsFasta, cdsDB
		)) {
			ExecutionResult mkldsResult = AbstractBinariesExecutor.executeCommand(
				LOG,
				false,
				this.binaries.getSplign(), 
				"-mklds", dirManager.getWorkingDirectoryPath()
			);
			if (mkldsResult.getExitStatus() != 0) {
				return mkldsResult;
			} else {
				mkldsResult = null;
			}
			
			ExecutionResult compartResult = AbstractBinariesExecutor.executeCommand(
				LOG,
				false,
				Arrays.asList(dirManager.getCompartmentsCallback()),
				this.binaries.getCompart(), 
				"-qdb", dirManager.getTargetFastaPath(),
				"-sdb", dirManager.getReferenceFastaPath()
			);
			if (compartResult.getExitStatus() != 0) {
				return compartResult;
			} else {
				compartResult = null;
			}
			
			ExecutionResult ldsdirResult = AbstractBinariesExecutor.executeCommand(
				LOG,
				false,
				Arrays.asList(dirManager.getLdsdirCallback()),
				this.binaries.getSplign(),
				"-ldsdir", dirManager.getWorkingDirectoryPath(),
				"-comps", dirManager.getCompartmentsPath()
			);
			if (ldsdirResult.getExitStatus() != 0) {
				return ldsdirResult;
			} else {
				ldsdirResult = null;
			}
			
			dirManager.createBedFile(ldsdirToBed(new FileReader(dirManager.getLdsdirPath())));
			
			ExecutionResult bedtoolsResult = AbstractBinariesExecutor.executeCommand(
				LOG,
				false,
				this.binaries.getBedtools(),
				"getfasta", 
				"-fi", dirManager.getReferenceFastaPath(),
				"-bed", dirManager.getBedPath(),
				"-fo", fasta.getFile().getAbsolutePath(),
				"-name"
			);
			if (bedtoolsResult.getExitStatus() != 0) {
				return bedtoolsResult;
			} else {
				bedtoolsResult = null;
			}
			
			mergeSequences(fasta);
			
			deleteNonATGSequences(fasta);
			
			return new DefaultExecutionResult(0);
		}
	}
	
	private static void deleteNonATGSequences(NucleotideFasta fasta) throws IOException, FastaParseException {
		final Path filteredFile = Files.createTempFile("bdbm-filtered", "fasta");
		
		try (PrintWriter pw = new PrintWriter(filteredFile.toFile())) {
			final FastaParser parser = new DefaultFastaParser();
			parser.addParseListener(new FastaParserAdapter() {
				String name, previousName;
				StringBuilder sequence, previousSequence;
				
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					this.name = sequenceName;
					this.sequence = new StringBuilder();
				}
				
				@Override
				public void sequenceFragmentRead(File file, String sequenceFragment) {
					if (this.sequence.length() > 0)
						this.sequence.append("\n");
					this.sequence.append(sequenceFragment);
				}
				
				@Override
				public void sequenceEnd(File file) {
					if (this.sequence.length() >= 3 && this.sequence.substring(0, 3).equalsIgnoreCase("ATG")) {
						if (this.previousName != null && this.previousSequence != null) {
							pw.println(this.previousName);
							pw.println(this.previousSequence);
						}
						
						this.previousName = this.name;
						this.previousSequence = this.sequence;
					} else {
						this.previousName = null;
						this.previousSequence = null;
					}
					
					this.name = null;
					this.sequence = null;
				}
				
				@Override
				public void parseEnd(File file) {
					if (this.previousName != null && this.previousSequence != null) {
						pw.println(this.previousSequence);
						pw.println(this.previousName);
					}
				}
			});
			
			parser.parse(fasta.getFile());
		}
		
		Files.move(filteredFile, fasta.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	private static void mergeSequences(NucleotideFasta fasta)
	throws IOException, FileNotFoundException {
		final File tempFasta = File.createTempFile("bdbm", ".fasta");
		tempFasta.deleteOnExit();
		
		final Map<String, AtomicInteger> count = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fasta.getFile()))) {
			String line;
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				
				if (line.isEmpty()) {
					throw new IOException("Illegal fasta format in file: " + fasta.getFile().getAbsolutePath());
				} else if (line.startsWith(">")) {
					if (count.containsKey(line)) {
						count.get(line).incrementAndGet();
					} else {
						count.put(line, new AtomicInteger(1));
					}
				}
			}
		}
		
		final Map<String, StringBuilder> sequences = new HashMap<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fasta.getFile()), 4*1024*1024);
			PrintWriter pw = new PrintWriter(tempFasta)
		) {
			String currentName = null;
			StringBuilder currentSequence = null;
			
			String line;
			boolean end = false;
			do {
				currentName = br.readLine();
				if (currentName == null || !currentName.startsWith(">"))
					throw new IOException("Illegal fasta format in file: " + fasta.getFile().getAbsolutePath());
				currentName = currentName.trim();
				
				currentSequence = new StringBuilder();
				while (true) {
					br.mark(4*1024*1024);
					line = br.readLine();
					
					if (line == null) {
						end = true;
						break;
					} else if (line.startsWith(">")) {
						br.reset();
						break;
					} else {
						currentSequence.append(line);
					}
				}
				
				if (currentSequence.length() == 0)
					throw new IOException("Illegal fasta format in file: " + fasta.getFile().getAbsolutePath());
				
				if (!sequences.containsKey(currentName)) {
					sequences.put(currentName, new StringBuilder());
				}
				sequences.get(currentName).append(currentSequence);
				
				if (count.get(currentName).decrementAndGet() == 0) {
					count.remove(currentName);
					
					pw.println(currentName);
					pw.println(sequences.remove(currentName));
				}
			} while (!end);
		}

		Files.move(tempFasta.toPath(), fasta.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	private static class DirectoryManager implements AutoCloseable {
		private final Path workingDirectory;
		private final Path targetFastaFile;
		private final Path referenceFastaFile;
		private Path compartmentsFile;
		private Path ldsdirFile;
		private Path bedFile;
		
		public DirectoryManager(
			NucleotideFasta referenceFasta,
			NucleotideDatabase referenceDB,
			NucleotideFasta targetFasta,
			NucleotideDatabase targetDB
		) throws IOException {
			this.workingDirectory = Files.createTempDirectory("bdbm_spligncompart");
			final Path targetFastaPath = targetFasta.getFile().toPath();
			final Path referenceFastaPath = referenceFasta.getFile().toPath();
			
			this.referenceFastaFile = this.workingDirectory.resolve("reference");
			this.targetFastaFile = this.workingDirectory.resolve("target");
			
			this.createSymlinksToDirFiles(referenceDB.getDirectory().getParentFile().toPath(), "reference");
			this.createSymlinksToDirFiles(targetDB.getDirectory().getParentFile().toPath(), "target");
			Files.createSymbolicLink(this.referenceFastaFile, referenceFastaPath);
			Files.createSymbolicLink(this.targetFastaFile, targetFastaPath);
		}
		
		private void createSymlinksToDirFiles(Path linksDir, final String filesname)
		throws IOException {
			Files.walkFileTree(linksDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file,	BasicFileAttributes attrs) 
				throws IOException {
					final String extension = FilenameUtils.getExtension(file.toString());
					
					Files.createSymbolicLink(workingDirectory.resolve(filesname + "." + extension), file);
					
					return FileVisitResult.CONTINUE;
				}
			});
		}
		
		public InputLineCallback getCompartmentsCallback() throws IOException {
			this.compartmentsFile = Files.createTempFile("bdbm", ".compartments");

			return new InputLineToFileCallback(this.compartmentsFile.toFile());
		}
		
		public InputLineCallback getLdsdirCallback() throws IOException {
			this.ldsdirFile = Files.createTempFile("bdbm", ".ldsdir");
			
			return new InputLineToFileCallback(this.ldsdirFile.toFile());
		}
		
		public void createBedFile(String text) throws IOException {
			this.bedFile = Files.createTempFile("bdbm", ".bed");
			Files.write(this.bedFile, text.getBytes());
		}
		
		public String getCompartmentsPath() {
			return compartmentsFile.toString();
		}
		
		public String getLdsdirPath() {
			return ldsdirFile.toString();
		}
		
		public String getBedPath() {
			return bedFile.toString();
		}
		
		public String getWorkingDirectoryPath() {
			return workingDirectory.toString();
		}
		
		public String getTargetFastaPath() {
			return targetFastaFile.toString();
		}
		
		public String getReferenceFastaPath() {
			return referenceFastaFile.toString();
		}
		
		@Override
		public void close() throws IOException {
			if (Boolean.valueOf(System.getProperty("spligncompart.deletetmpfiles", "true"))) {
				if (this.compartmentsFile != null)
					Files.deleteIfExists(this.compartmentsFile);
				if (this.ldsdirFile != null)
					Files.deleteIfExists(this.ldsdirFile);
				if (this.bedFile != null)
					Files.deleteIfExists(this.bedFile);
				
				Files.walkFileTree(this.workingDirectory, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file,	BasicFileAttributes attrs) 
					throws IOException {
						Files.delete(file);
						
						return FileVisitResult.CONTINUE;
					}
					
					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
						Files.delete(dir);
						
						return FileVisitResult.CONTINUE;
					}
				});
			}
		}
	}

	private static final class InputLineToFileCallback implements InputLineCallback {
		private final File file;
		private PrintWriter pw;
		
		public InputLineToFileCallback(File file) {
			this.file = file;
		}

		@Override
		public void inputFinished() {
			if (this.pw != null)
				this.pw.close();
		}

		@Override
		public void inputStarted() {
			try {
				this.pw = new PrintWriter(this.file);
			} catch (FileNotFoundException e) {
				LOG.error("Error creating writer for file: " + this.file.getAbsolutePath(), e);
			}
		}

		@Override
		public void line(String line) {
			if (this.pw != null)
				this.pw.println(line);
		}

		@Override
		public void error(String message, Exception e) {
		}

		@Override
		public void info(String message) {
		}
	}

	public static String ldsdirToBed(Reader input) throws IOException {
		try (final BufferedReader reader = new BufferedReader(input)) {
			String line;
			
			final StringBuilder sb = new StringBuilder();
			StringBuilder currentGene = null;
			String lastSequence = null;
			
			int offsetCount = 1;
			while ((line = reader.readLine()) != null) {
				final String[] split = line.split("\t");
				
				if (split.length == 11) {
					if (isTargetSequence(split[9])) {
						line = line.replaceAll("-\t-", "0\t0");
					
						final Integer startOffset = safeParseInt(split[5]);
						final Integer endOffset = safeParseInt(split[6]);
						final Integer startPosition = safeParseInt(split[7]);
						final Integer endPosition = safeParseInt(split[8]);
						
						if (startOffset != null && endOffset != null
							&& startPosition != null && endPosition != null
							&& (startOffset < endOffset) 
							&& (startPosition < endPosition) 
							&& (startOffset == 1 || startOffset == offsetCount) 
						) {
							if (startOffset == 1) {
								checkAndAppend(currentGene, lastSequence, sb);
								currentGene = new StringBuilder();
								lastSequence = null;
							}
							
							currentGene.append(split[2]).append('\t') // Name
								.append(startPosition - 1).append('\t') // startPosition with correction
								.append(split[8]).append('\t') // endPosition
								.append(split[1]).append('\t') // index
								.append(split[9]) // sequence
							.append('\n');
							
							lastSequence = split[9];
							offsetCount = endOffset + 1;
						} else {
							checkAndAppend(currentGene, lastSequence, sb); 
							
							offsetCount = 1;
							currentGene = null;
							lastSequence = null;
						}
					}
				}
			}
			
			checkAndAppend(currentGene, lastSequence, sb);
			
			return sb.toString();
		}
	}

	public static void checkAndAppend(
		StringBuilder currentGene, String lastSequence, StringBuilder result
	) {
		if (currentGene != null && isValidEnd(lastSequence)) {
			result.append(currentGene);
		}
	}

	private static Integer safeParseInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}
	
	private static boolean isTargetSequence(String sequence) {
		final List<String> targetExons = Arrays.asList(
			"  <exon>TA", "  <exon>TG", "  <exon>GT",
			"AG<exon>TA", "AG<exon>TG", "AG<exon>GT",
			"<poly-A>", "  <exon>  "
		);
		
		return targetExons.contains(sequence);
	}
	
	private static boolean isValidEnd(String lastSequence) {
		final List<String> targetExons = Arrays.asList(
			"  <exon>TA", "  <exon>TG",
			"AG<exon>TA", "AG<exon>TG",
			"<poly-A>"
		);
		
		return targetExons.contains(lastSequence);
	}
}
