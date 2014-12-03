package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.SplignBinaries;

public interface SplignBinariesChecker extends BinariesChecker<SplignBinaries> {
	public void checkSplign() throws BinaryCheckException;
}
