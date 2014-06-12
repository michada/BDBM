package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;

public class DefaultBLASTBinaryToolsFactory implements BLASTBinaryToolsFactory {
	private BLASTBinaries bBinaries;
	
	@Override
	public boolean isValidFor(BLASTBinaries bBinaries) {
		return false;
	}

	@Override
	public void setBinaries(BLASTBinaries bBinaries)
	throws BinaryCheckException {
		DefaultBLASTBinariesChecker.checkAll(bBinaries);
		
		this.bBinaries = bBinaries;
	}

	@Override
	public BLASTBinariesChecker createChecker() {
		return new DefaultBLASTBinariesChecker(this.bBinaries);
	}

	@Override
	public BLASTBinariesExecutor createExecutor() {
		try {
			return new DefaultBLASTBinariesExecutor(this.bBinaries);
		} catch (BinaryCheckException e) {
			throw new RuntimeException(e);
		}
	}
}
