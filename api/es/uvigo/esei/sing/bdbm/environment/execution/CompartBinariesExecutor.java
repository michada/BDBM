package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;
import es.uvigo.esei.sing.bdbm.environment.execution.AbstractBinariesExecutor.InputLineCallback;

public interface CompartBinariesExecutor extends BinariesExecutor<CompartBinaries> {
	public boolean checkCompartBinaries(CompartBinaries bBinaries);
	
	public ExecutionResult compart(String qdb, String sdb, InputLineCallback ... callbacks)
	throws ExecutionException, InterruptedException;
}
