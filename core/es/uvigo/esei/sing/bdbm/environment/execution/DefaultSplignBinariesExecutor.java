package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.binaries.SplignBinaries;

public class DefaultSplignBinariesExecutor
extends AbstractBinariesExecutor<SplignBinaries>
implements SplignBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(SplignBinariesExecutor.class);
	
	public DefaultSplignBinariesExecutor() {}

	public DefaultSplignBinariesExecutor(SplignBinaries nBinaries)
	throws BinaryCheckException {
		this.setBinaries(nBinaries);
	}

	@Override
	public void setBinaries(
		SplignBinaries nBinaries
	) throws BinaryCheckException {
		DefaultSplignBinariesChecker.checkAll(nBinaries);
		
		this.binaries = nBinaries;
	}
	
	@Override
	public boolean checkSplignBinaries(SplignBinaries bBinaries) {
		try {
			DefaultSplignBinariesChecker.checkAll(bBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}
	
	@Override
	public ExecutionResult mklds(String path, InputLineCallback ... callbacks)
	throws ExecutionException, InterruptedException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			false,
			Arrays.asList(callbacks),
			this.binaries.getSplign(), 
			"-mklds", path
		);
	}
	
	@Override
	public ExecutionResult ldsdir(
		String ldsdir, String comps,
		InputLineCallback... callbacks
	) throws ExecutionException, InterruptedException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			false,
			Arrays.asList(callbacks),
			this.binaries.getSplign(),
			"-ldsdir", ldsdir,
			"-comps", comps
		);
	}
}
