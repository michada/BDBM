package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;

public interface CompartBinariesChecker extends BinariesChecker<CompartBinaries> {
	public void checkCompart() throws BinaryCheckException;
}
