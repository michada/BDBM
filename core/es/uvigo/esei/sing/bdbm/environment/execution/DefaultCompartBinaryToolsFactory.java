package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;

public class DefaultCompartBinaryToolsFactory implements CompartBinaryToolsFactory {
	private CompartBinaries nBinaries;
	
	@Override
	public boolean isValidFor(CompartBinaries nBinaries) {
		return false;
	}

	@Override
	public void setBinaries(CompartBinaries nBinaries)
	throws BinaryCheckException {
		DefaultCompartBinariesChecker.checkAll(nBinaries);
		
		this.nBinaries = nBinaries;
	}

	@Override
	public CompartBinariesChecker createChecker() {
		return new DefaultCompartBinariesChecker(this.nBinaries);
	}

	@Override
	public CompartBinariesExecutor createExecutor() {
		try {
			return new DefaultCompartBinariesExecutor(this.nBinaries);
		} catch (BinaryCheckException e) {
			throw new RuntimeException(e);
		}
	}
}
