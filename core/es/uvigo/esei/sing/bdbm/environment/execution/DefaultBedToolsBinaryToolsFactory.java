package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.BedToolsBinaries;

public class DefaultBedToolsBinaryToolsFactory implements BedToolsBinaryToolsFactory {
	private BedToolsBinaries nBinaries;
	
	@Override
	public boolean isValidFor(BedToolsBinaries nBinaries) {
		return false;
	}

	@Override
	public void setBinaries(BedToolsBinaries nBinaries)
	throws BinaryCheckException {
		DefaultBedToolsBinariesChecker.checkAll(nBinaries);
		
		this.nBinaries = nBinaries;
	}

	@Override
	public BedToolsBinariesChecker createChecker() {
		return new DefaultBedToolsBinariesChecker(this.nBinaries);
	}

	@Override
	public BedToolsBinariesExecutor createExecutor() {
		try {
			return new DefaultBedToolsBinariesExecutor(this.nBinaries);
		} catch (BinaryCheckException e) {
			throw new RuntimeException(e);
		}
	}
}
