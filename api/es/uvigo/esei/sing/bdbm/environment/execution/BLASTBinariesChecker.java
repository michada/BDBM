package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;

public interface BLASTBinariesChecker extends BinariesChecker<BLASTBinaries> {
	public void checkMakeBlastDB() throws BinaryCheckException;

	public void checkBlastDBAliasTool() throws BinaryCheckException;

	public void checkBlastDBCmd() throws BinaryCheckException;

	public void checkBlastN() throws BinaryCheckException;

	public void checkTBlastX() throws BinaryCheckException;

	public void checkTBlastN() throws BinaryCheckException;
}
