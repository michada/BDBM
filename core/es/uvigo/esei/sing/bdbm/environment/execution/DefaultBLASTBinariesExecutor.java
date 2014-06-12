package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import es.uvigo.esei.sing.bdbm.LogConfiguration;
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

public class DefaultBLASTBinariesExecutor implements BLASTBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultBLASTBinariesExecutor.class);
	
	private BLASTBinaries bBinaries;
	
	public DefaultBLASTBinariesExecutor() {}

	public DefaultBLASTBinariesExecutor(
		BLASTBinaries bBinaries
	) throws BinaryCheckException {
		this.setBinaries(bBinaries);
	}

	@Override
	public void setBinaries(
		BLASTBinaries bBinaries
	) throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkAll(bBinaries);
		
		this.bBinaries = bBinaries;
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
		return DefaultBLASTBinariesExecutor.executeCommand(
			this.bBinaries.getMakeBlastDB(),
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
		
		return DefaultBLASTBinariesExecutor.executeCommand(
			this.bBinaries.getBlastDBAliasTool(),
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
		return DefaultBLASTBinariesExecutor.executeCommand(
			this.bBinaries.getBlastDBCmd(),
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
		return DefaultBLASTBinariesExecutor.executeCommand(
			this.bBinaries.getBlastDBCmd(),
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
		
		return DefaultBLASTBinariesExecutor.executeCommand(
			this.bBinaries.getBlast(blastType), 
			"-query", queryFile.getAbsolutePath(),
			"-db", database.getDirectory().getAbsolutePath(),
			"-evalue", expectedValue.toPlainString(),
			blastType.getFilterParam(), filter ? "yes" : "no",
			"-out", outFile.getAbsolutePath()
		);
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
		final List<String> alignments = new ArrayList<String>();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(entry.getOutFile()));
			
			String line;
			boolean startFound = false;
			while ((line = br.readLine()) != null) {
				if (startFound) {
					if (line.trim().isEmpty()|| 
						!(line.contains("|") && line.contains(" ")))
						continue;
					if (line.startsWith(">")) break;
					
					
					final String sequence = line.substring(line.indexOf('|') + 1, line.indexOf(' '));
					alignments.add(sequence);
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
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					LOG.warn("Error closing reader for file: " + entry.getOutFile(), e);
				}
		}
		
		return alignments;
	}

	protected static String commandToString(String command, String ... params) {
		final StringBuilder sb = new StringBuilder(command);
		
		for (String param : params) {
			sb.append(' ').append(param);
		}
		
		return sb.toString();
	}
	
	protected static ExecutionResult executeCommand(String command, String ... params)
	throws ExecutionException, InterruptedException {
		final String commandString = commandToString(command, params);
		LOG.info("Executing command: " + commandString);
		
		final Runtime runtime = Runtime.getRuntime();
		
		try {
			final String[] cmdarray = new String[params.length+1];
			cmdarray[0] = command;
			System.arraycopy(params, 0, cmdarray, 1, params.length);
			
			System.out.println(Arrays.toString(cmdarray));
			
//			final Process process = runtime.exec(commandString);
			final Process process = runtime.exec(cmdarray);
			
			final LoggerThread inThread = new LoggerThread(
				commandString, process.getInputStream(), LOG, LogConfiguration.MARKER_EXECUTION_STD
			);
			final LoggerThread errThread = new LoggerThread(
				commandString, process.getErrorStream(), LOG, LogConfiguration.MARKER_EXECUTION_ERROR
			);
			inThread.start();
			errThread.start();
			
			final int outputCode = process.waitFor();
			
			inThread.join();
			errThread.join();
			
			return new DefaultExecutionResult(outputCode, inThread.getText(), errThread.getText());
		} catch (IOException e) {
			LOG.error(LogConfiguration.MARKER_EXECUTION_ERROR, "Error executing command: " + commandString, e);
			
			throw new ExecutionException(-1, e, command);
		}
	}

	private static class LoggerThread extends Thread {
		private final String command;
		private final BufferedReader reader;
		private final Logger logger;
		private final Marker marker;
		private final StringBuilder sb;
		
		public LoggerThread(String command, InputStream is, Logger logger, Marker marker) {
			this.setDaemon(true);
			
			this.command = command;
			this.reader = new BufferedReader(new InputStreamReader(is));
			this.logger = logger;
			this.marker = marker;
			this.sb = new StringBuilder();
		}
		
		@Override
		public void run() {
			try {
				this.logger.info(this.marker, "Executing command: " + command);
				
				String line;
				while ((line = this.reader.readLine()) != null) {
					this.logger.info(this.marker, line);
					sb.append(line);
				}
			} catch (IOException e) {
				this.logger.error(this.marker, "Error executing command", e);
			}
		}
		
		public String getText() {
			return this.sb.toString();
		}
	}
}
