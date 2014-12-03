package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;

public class DefaultCompartBinariesExecutor
extends AbstractBinariesExecutor<CompartBinaries>
implements CompartBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(CompartBinariesExecutor.class);
	
	public DefaultCompartBinariesExecutor() {}

	public DefaultCompartBinariesExecutor(CompartBinaries nBinaries)
	throws BinaryCheckException {
		this.setBinaries(nBinaries);
	}

	@Override
	public void setBinaries(
		CompartBinaries nBinaries
	) throws BinaryCheckException {
		DefaultCompartBinariesChecker.checkAll(nBinaries);
		
		this.binaries = nBinaries;
	}
	
	@Override
	public boolean checkCompartBinaries(CompartBinaries bBinaries) {
		try {
			DefaultCompartBinariesChecker.checkAll(bBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}
	
	@Override
	public ExecutionResult compart(String qdb, String sdb, InputLineCallback ... callbacks)
	throws ExecutionException, InterruptedException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			false,
			Arrays.asList(callbacks),
			this.binaries.getCompart(), 
			"-qdb", qdb,
			"-sdb", sdb
		);
	}
}
