package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;

public class DefaultCompartBinariesChecker implements CompartBinariesChecker {
	private CompartBinaries eBinaries;
	
	public DefaultCompartBinariesChecker() {
	}
	
	public DefaultCompartBinariesChecker(CompartBinaries eBinaries) {
		this.eBinaries = eBinaries;
	}
	
	public static void checkAll(CompartBinaries eBinaries)
	throws BinaryCheckException {
		new DefaultCompartBinariesChecker(eBinaries).checkAll();
	}

	@Override
	public void setBinaries(CompartBinaries eBinaries) {
		this.eBinaries = eBinaries;
	}

	@Override
	public void checkAll() throws BinaryCheckException {
		this.checkCompart();
	}

	@Override
	public void checkCompart() throws BinaryCheckException {
		checkCommand(this.eBinaries.getCompart(), "compart");
		
	}

	protected static void checkCommand(String command, String program) throws BinaryCheckException {
		final Runtime runtime = Runtime.getRuntime();
		
		try {
			final Process process = runtime.exec(new String[] { command, "-version" });
			
			final BufferedReader br = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
			
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
			if (!sb.toString().startsWith(program)) {
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
