package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import es.uvigo.esei.sing.bdbm.LogConfiguration;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideORF;

public class DefaultEMBOSSBinariesExecutor implements EMBOSSBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultEMBOSSBinariesExecutor.class);
	
	private EMBOSSBinaries eBinaries;
	
	public DefaultEMBOSSBinariesExecutor() {}

	public DefaultEMBOSSBinariesExecutor(
		EMBOSSBinaries bBinaries
	) throws BinaryCheckException {
		this.setBinaries(bBinaries);
	}

	@Override
	public void setBinaries(
		EMBOSSBinaries bBinaries
	) throws BinaryCheckException {
		DefaultEMBOSSBinariesChecker.checkAll(bBinaries);
		
		this.eBinaries = bBinaries;
	}
	
	@Override
	public boolean checkEMBOSSBinaries(EMBOSSBinaries bBinaries) {
		try {
			DefaultEMBOSSBinariesChecker.checkAll(bBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}

	@Override
	public ExecutionResult executeGetORF(
		NucleotideFasta fasta, 
		NucleotideORF orf,
		int minSize,
		int maxSize
	) throws InterruptedException, ExecutionException, IOException {
		return DefaultEMBOSSBinariesExecutor.executeCommand(
			this.eBinaries.getGetORF(), 
			"-snucleotide", fasta.getFile().getAbsolutePath(),
			"-outseq", orf.getFile().getAbsolutePath(),
			"-table", "0",
			"-minsize", Integer.toString(minSize),
			"-maxsize", Integer.toString(maxSize),
			"-find", "3"
		);
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
