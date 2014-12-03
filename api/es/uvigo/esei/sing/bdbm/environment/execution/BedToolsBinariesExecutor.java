package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.BedToolsBinaries;
import es.uvigo.esei.sing.bdbm.environment.execution.AbstractBinariesExecutor.InputLineCallback;

public interface BedToolsBinariesExecutor extends BinariesExecutor<BedToolsBinaries> {
	public boolean checkBedToolsBinaries(BedToolsBinaries bBinaries);
	
	public ExecutionResult getfasta(
		String fi, String bed, String fo, InputLineCallback ... callbacks
	) throws ExecutionException, InterruptedException;
}
