package es.uvigo.esei.sing.bdbm.environment.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;

public class DefaultNCBIBinariesExecutor
extends AbstractBinariesExecutor<NCBIBinaries>
implements NCBIBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultNCBIBinariesExecutor.class);
	
	public DefaultNCBIBinariesExecutor() {}

	public DefaultNCBIBinariesExecutor(NCBIBinaries nBinaries)
	throws BinaryCheckException {
		this.setBinaries(nBinaries);
	}

	@Override
	public void setBinaries(
		NCBIBinaries nBinaries
	) throws BinaryCheckException {
		DefaultNCBIBinariesChecker.checkAll(nBinaries);
		
		this.binaries = nBinaries;
	}
	
	@Override
	public boolean checkNCBIBinaries(NCBIBinaries bBinaries) {
		try {
			DefaultNCBIBinariesChecker.checkAll(bBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}
}
