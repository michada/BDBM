package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;

public interface NCBIBinariesChecker extends BinariesChecker<NCBIBinaries> {
	public void checkSplign() throws BinaryCheckException;

	public void checkCompart() throws BinaryCheckException;

	public void checkBedtools() throws BinaryCheckException;
}
