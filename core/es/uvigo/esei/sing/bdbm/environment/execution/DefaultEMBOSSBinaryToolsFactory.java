package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;

public class DefaultEMBOSSBinaryToolsFactory implements EMBOSSBinaryToolsFactory {
	private EMBOSSBinaries bBinaries;
	
	@Override
	public boolean isValidFor(EMBOSSBinaries eBinaries) {
		return false;
	}

	@Override
	public void setBinaries(EMBOSSBinaries eBinaries)
	throws BinaryCheckException {
		DefaultEMBOSSBinariesChecker.checkAll(eBinaries);
		
		this.bBinaries = eBinaries;
	}

	@Override
	public EMBOSSBinariesChecker createChecker() {
		return new DefaultEMBOSSBinariesChecker(this.bBinaries);
	}

	@Override
	public EMBOSSBinariesExecutor createExecutor() {
		try {
			return new DefaultEMBOSSBinariesExecutor(this.bBinaries);
		} catch (BinaryCheckException e) {
			throw new RuntimeException(e);
		}
	}
}
