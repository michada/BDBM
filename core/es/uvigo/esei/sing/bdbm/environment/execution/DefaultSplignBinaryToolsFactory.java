package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.SplignBinaries;

public class DefaultSplignBinaryToolsFactory implements SplignBinaryToolsFactory {
	private SplignBinaries nBinaries;
	
	@Override
	public boolean isValidFor(SplignBinaries nBinaries) {
		return false;
	}

	@Override
	public void setBinaries(SplignBinaries nBinaries)
	throws BinaryCheckException {
		DefaultSplignBinariesChecker.checkAll(nBinaries);
		
		this.nBinaries = nBinaries;
	}

	@Override
	public SplignBinariesChecker createChecker() {
		return new DefaultSplignBinariesChecker(this.nBinaries);
	}

	@Override
	public SplignBinariesExecutor createExecutor() {
		try {
			return new DefaultSplignBinariesExecutor(this.nBinaries);
		} catch (BinaryCheckException e) {
			throw new RuntimeException(e);
		}
	}
}
