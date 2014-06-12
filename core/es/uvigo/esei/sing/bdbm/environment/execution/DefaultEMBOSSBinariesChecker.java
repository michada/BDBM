package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;

public class DefaultEMBOSSBinariesChecker implements EMBOSSBinariesChecker {
	private EMBOSSBinaries eBinaries;
	
	public DefaultEMBOSSBinariesChecker() {
	}
	
	public DefaultEMBOSSBinariesChecker(EMBOSSBinaries eBinaries) {
		this.eBinaries = eBinaries;
	}
	
	public static void checkAll(EMBOSSBinaries eBinaries)
	throws BinaryCheckException {
		new DefaultEMBOSSBinariesChecker(eBinaries).checkAll();
	}

	@Override
	public void setBinaries(EMBOSSBinaries eBinaries) {
		this.eBinaries = eBinaries;
	}

	@Override
	public void checkAll() throws BinaryCheckException {
		this.checkGetORF();
	}
	
	@Override
	public void checkGetORF() throws BinaryCheckException {
		DefaultEMBOSSBinariesChecker.checkCommand(this.eBinaries.getGetORF());
	}

	protected static void checkCommand(String command) throws BinaryCheckException {
		final Runtime runtime = Runtime.getRuntime();
		
//		command += " -version";
		
		try {
			final Process process = runtime.exec(new String[] { command, "-version" });
			
			final BufferedReader br = new BufferedReader(
				new InputStreamReader(process.getErrorStream()));
			
			final StringBuilder sb = new StringBuilder();
			
			String line;
			int countLines = 0;
			while ((line = br.readLine()) != null) {
				sb.append(line).append('\n');
				countLines++;
			}
			
			if (countLines != 1) {
				throw new BinaryCheckException("Unrecognized version output", command);
			}

			// TODO: Better parsing?
			if (!sb.toString().startsWith("EMBOSS:")) {
				throw new BinaryCheckException("Unrecognized version output", command);
			}
			
			final int exitStatus = process.waitFor();
			if (exitStatus != 0) {
				throw new BinaryCheckException(
					"Invalid exit status: " + exitStatus, 
					command
				);
			}
		} catch (Exception e) {
			throw new BinaryCheckException("Error while checking version", e, command);
		}
	}
}
