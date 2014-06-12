package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;

public class DefaultNCBIBinaryToolsFactory implements NCBIBinaryToolsFactory {
	private NCBIBinaries nBinaries;
	
	@Override
	public boolean isValidFor(NCBIBinaries nBinaries) {
		return false;
	}

	@Override
	public void setBinaries(NCBIBinaries nBinaries)
	throws BinaryCheckException {
		DefaultNCBIBinariesChecker.checkAll(nBinaries);
		
		this.nBinaries = nBinaries;
	}

	@Override
	public NCBIBinariesChecker createChecker() {
		return new DefaultNCBIBinariesChecker(this.nBinaries);
	}

	@Override
	public NCBIBinariesExecutor createExecutor() {
		try {
			return new DefaultNCBIBinariesExecutor(this.nBinaries);
		} catch (BinaryCheckException e) {
			throw new RuntimeException(e);
		}
	}
}
