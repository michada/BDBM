package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTType;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export.ExportEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry.ProteinQuery;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

public class DefaultBLASTBinariesExecutor
extends AbstractBinariesExecutor<BLASTBinaries>
implements BLASTBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultBLASTBinariesExecutor.class);
	
	public DefaultBLASTBinariesExecutor() {}

	public DefaultBLASTBinariesExecutor(BLASTBinaries bBinaries)
	throws BinaryCheckException {
		this.setBinaries(bBinaries);
	}

	@Override
	public void setBinaries(BLASTBinaries binaries) 
	throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkAll(binaries);
		
		super.setBinaries(binaries);
	}
	
	@Override
	public boolean checkBLASTBinaries(BLASTBinaries bBinaries) {
		try {
			DefaultBLASTBinariesChecker.checkAll(bBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}

	@Override
	public ExecutionResult executeMakeBlastDB(
		File inputFasta, Database database
	) throws InterruptedException, ExecutionException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			this.binaries.getMakeBlastDB(),
			"-in", inputFasta.getAbsolutePath(),
			"-parse_seqids",
			"-dbtype", database.getType().getDBType(),
			"-out", new File(database.getDirectory(), database.getName()).getAbsolutePath()
		);
	}

	@Override
	public ExecutionResult executeBlastDBAliasTool(
		Database database, Database[] databases)
	throws InterruptedException, ExecutionException, IOException {
		final StringBuilder sbDBList = new StringBuilder();
		
		for (Database inputDB : databases) {
			if (sbDBList.length() > 0) sbDBList.append(' ');
			sbDBList.append(new File(inputDB.getDirectory(), inputDB.getName()).getAbsolutePath());
		}
		
		final File dbFile = new File(database.getDirectory(), database.getName());
		if (!dbFile.isDirectory() && !dbFile.mkdirs())
			throw new IOException("Database directory could not be created: " + dbFile);
		
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			this.binaries.getBlastDBAliasTool(),
			"-dblist", sbDBList.toString(),
			"-dbtype", database.getType().getDBType(),
			"-out", dbFile.getAbsolutePath(),
			"-title", database.getName()
		);
	}

	@Override
	public ExecutionResult executeBlastDBCMD(
		Database database, SearchEntry searchEntry, String entry
	) throws InterruptedException, ExecutionException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			this.binaries.getBlastDBCmd(),
			"-db", new File(database.getDirectory(), database.getName()).getAbsolutePath(),
			"-entry", entry,
			"-outfmt", "%f",
			"-out",	new File(searchEntry.getDirectory(), entry + ".txt").getAbsolutePath()
		);
	}

	@Override
	public ExecutionResult executeBlastDBCMD(
		Database database, ExportEntry exportEntry, String entry
	) throws InterruptedException, ExecutionException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			this.binaries.getBlastDBCmd(),
			"-db", database.getDirectory().getAbsolutePath(),
			"-entry", entry,
			"-outfmt", "%f",
			"-out",	new File(exportEntry.getBaseFile(), entry + ".txt").getAbsolutePath()
		);
	}

	@Override
	public ExecutionResult executeBlast(
		BLASTType blastType, 
		Database database, 
		File queryFile,
		Export export, 
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		final File outDirectory = new File(export.getBaseFile(), outputName);
		final File outFile = new File(outDirectory, outputName + ".out");
		
		if (!outDirectory.isDirectory() && !outDirectory.mkdirs()) {
			throw new IOException("Output directory could not be created: " + outDirectory);
		}
		
//		final File[] subFastas = FastaUtils.splitFastaIntoFiles(queryFile);
//		if (subFastas.length == 1) {
			return AbstractBinariesExecutor.executeCommand(
				LOG,
				this.binaries.getBlast(blastType), 
				"-query", queryFile.getAbsolutePath(),
				"-db", database.getDirectory().getAbsolutePath(),
				"-evalue", expectedValue.toPlainString(),
				blastType.getFilterParam(), filter ? "yes" : "no",
				"-out", outFile.getAbsolutePath()
			);
//		} else {
//			final StringBuilder sbOutput = new StringBuilder();
//			final StringBuilder sbError = new StringBuilder();
//			int exitStatus = 0;
//			
//			for (File fastaFile : subFastas) {
//				final File outTmpFile = File.createTempFile("bdbm", "out");
//				outTmpFile.deleteOnExit();
//				
//				final ExecutionResult result = AbstractBinariesExecutor.executeCommand(
//					LOG,
//					this.binaries.getBlast(blastType), 
//					"-query", fastaFile.getAbsolutePath(),
//					"-db", database.getDirectory().getAbsolutePath(),
//					"-evalue", expectedValue.toPlainString(),
//					blastType.getFilterParam(), filter ? "yes" : "no",
//					"-out", outTmpFile.getAbsolutePath()
//				);
//				
//				
//				if (result.getExitStatus() != 0)
//					exitStatus = result.getExitStatus();
//				sbOutput.append(result.getOutput()).append('\n');
//				sbError.append(result.getError()).append('\n');
//				
//				FileUtils.writeLines(outFile, FileUtils.readLines(outTmpFile), true);
//				
//				outTmpFile.delete();
//				fastaFile.delete();
//			}
//			
//			return new DefaultExecutionResult(exitStatus, sbOutput.toString(), sbOutput.toString());
//		}
	}

	@Override
	public ExecutionResult executeBlast(
		BLASTType blastType, 
		Database database, 
		SearchEntry.Query query,
		Export export, 
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		return this.executeBlast(blastType, database, query.getBaseFile(), export, expectedValue, filter, outputName);
	}

	@Override
	public ExecutionResult executeBlastN(
		NucleotideDatabase database,
		File queryFile, 
		NucleotideExport export, 
		BigDecimal expectedValue,
		boolean filter, 
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.BLASTN, 
			database,
			queryFile,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}
	
	@Override
	public ExecutionResult executeBlastN(
		NucleotideDatabase database, 
		NucleotideSearchEntry.NucleotideQuery query,
		NucleotideExport export, 
		BigDecimal expectedValue,
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.BLASTN, 
			database,
			query,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}

	@Override
	public ExecutionResult executeBlastP(
		ProteinDatabase database,
		File queryFile,
		ProteinExport export, 
		BigDecimal expectedValue,
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.BLASTP, 
			database,
			queryFile,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}

	@Override
	public ExecutionResult executeBlastP(
		ProteinDatabase database,
		ProteinQuery query, 
		ProteinExport export, 
		BigDecimal expectedValue,
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.BLASTP, 
			database,
			query,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}

	@Override
	public ExecutionResult executeTBlastX(
		NucleotideDatabase database, 
		File queryFile,
		NucleotideExport export, 
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException ,ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.TBLASTX, 
			database,
			queryFile,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}

	@Override
	public ExecutionResult executeTBlastX(
		NucleotideDatabase database, 
		NucleotideSearchEntry.NucleotideQuery query,
		NucleotideExport export, 
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException ,ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.TBLASTX, 
			database,
			query,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}

	@Override
	public ExecutionResult executeTBlastN(
		NucleotideDatabase database, 
		File queryFile,
		NucleotideExport export, 
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.TBLASTN,  
			database,
			queryFile,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}

	@Override
	public ExecutionResult executeTBlastN(
		NucleotideDatabase database, 
		ProteinSearchEntry.ProteinQuery query,
		NucleotideExport export, 
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException {
		return this.executeBlast(
			BLASTType.TBLASTN,  
			database,
			query,
			export, 
			expectedValue, 
			filter,
			outputName
		);
	}
	
	@Override
	public List<String> extractSignificantSequences(ExportEntry entry) 
	throws IOException {
		final Set<String> alignments = new HashSet<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(entry.getOutFile()));) {
			String line;
			boolean startFound = false;
			while ((line = br.readLine()) != null) {
				if (startFound) {
					if (line.startsWith("lcl|")) {
						final String sequence = line.substring(line.indexOf('|') + 1, line.indexOf(' '));
						alignments.add(sequence);
					} else if (line.startsWith(">lcl")) {
						startFound = false;
					}
				} else {
					startFound = line.startsWith("Sequences producing significant alignments");
				}
			}
		} catch (FileNotFoundException e) {
			LOG.warn("File not found: " + entry.getOutFile(), e);
			throw e;
		} catch (IOException e) {
			LOG.warn("Error reading file: " + entry.getOutFile(), e);
			throw e;
		}
		
		return new ArrayList<>(alignments);
	}
}
