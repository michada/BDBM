package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

public interface NCBIBinariesExecutor extends BinariesExecutor<NCBIBinaries> {
	public boolean checkNCBIBinaries(NCBIBinaries bBinaries);

	public ExecutionResult splignCompart(
		NucleotideFasta referenceFasta, 
		NucleotideDatabase referenceDB, 
		NucleotideFasta targetFasta,
		NucleotideDatabase targetDB, 
		NucleotideFasta outputFasta
	) throws InterruptedException, ExecutionException, IOException;
}
