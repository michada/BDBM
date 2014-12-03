package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.BedToolsBinaries;

public interface BedToolsBinariesChecker extends BinariesChecker<BedToolsBinaries> {
	public void checkBedtools() throws BinaryCheckException;
}
