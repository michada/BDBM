package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;

public interface EMBOSSBinariesChecker extends BinariesChecker<EMBOSSBinaries> {
	public void checkGetORF() throws BinaryCheckException;
}
