package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;

public class DefaultBLASTBinariesChecker implements BLASTBinariesChecker {
	private BLASTBinaries bBinaries;
	
	public DefaultBLASTBinariesChecker() {
	}
	
	public DefaultBLASTBinariesChecker(BLASTBinaries bBinaries) {
		this.bBinaries = bBinaries;
	}
	
	public static void checkAll(BLASTBinaries bBinaries)
	throws BinaryCheckException {
		new DefaultBLASTBinariesChecker(bBinaries).checkAll();
	}

	@Override
	public void setBinaries(BLASTBinaries bBinaries) {
		this.bBinaries = bBinaries;
	}

	@Override
	public void checkAll() throws BinaryCheckException {
		this.checkMakeBlastDB();
		this.checkBlastDBAliasTool();
		this.checkBlastDBCmd();
		this.checkBlastN();
		this.checkTBlastX();
		this.checkTBlastN();
	}

	@Override
	public void checkMakeBlastDB() throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkCommand(this.bBinaries.getMakeBlastDB());
	}

	@Override
	public void checkBlastDBAliasTool() throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkCommand(this.bBinaries.getBlastDBAliasTool());
	}

	@Override
	public void checkBlastDBCmd() throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkCommand(this.bBinaries.getBlastDBCmd());
	}

	@Override
	public void checkBlastN() throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkCommand(this.bBinaries.getBlastN());
	}

	@Override
	public void checkTBlastX() throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkCommand(this.bBinaries.getTBlastX());
	}

	@Override
	public void checkTBlastN() throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkCommand(this.bBinaries.getTBlastN());
	}

	protected static void checkCommand(String command) throws BinaryCheckException {
		final Runtime runtime = Runtime.getRuntime();
		
		command += " -version";
		
		try {
			final Process process = runtime.exec(command);
			
			final BufferedReader br = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
			
			StringBuilder sb = new StringBuilder();
			
			String line;
			int countLines = 0;
			while ((line = br.readLine()) != null) {
				sb.append(line).append('\n');
				countLines++;
			}
			
			if (countLines != 2) {
				throw new BinaryCheckException("Unrecognized version output", command);
			}

			final String[] lines = sb.toString().split("\n");
			// TODO: Better parsing?
			if (!lines[1].startsWith("Package")) {
				throw new BinaryCheckException("Unrecognized version output", command);
			}
			
			final int exitStatus = process.waitFor();
			if (exitStatus != 0) {
				throw new BinaryCheckException(
					"Invalid exit status: " + exitStatus, 
					command
				);
			}
		} catch (IOException e) {
			throw new BinaryCheckException("Error while checking version", e, command);
		} catch (InterruptedException e) {
			throw new BinaryCheckException("Error while checking version", e, command);
		}
	}
}
