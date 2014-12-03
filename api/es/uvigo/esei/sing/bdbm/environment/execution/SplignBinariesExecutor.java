package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.SplignBinaries;
import es.uvigo.esei.sing.bdbm.environment.execution.AbstractBinariesExecutor.InputLineCallback;

public interface SplignBinariesExecutor extends BinariesExecutor<SplignBinaries> {
	public boolean checkSplignBinaries(SplignBinaries bBinaries);
	
	public ExecutionResult mklds(String path, InputLineCallback ... callbacks)
	throws ExecutionException, InterruptedException;
	
	public ExecutionResult ldsdir(String ldsdir, String comps, InputLineCallback ... callbacks)
	throws ExecutionException, InterruptedException;
}
