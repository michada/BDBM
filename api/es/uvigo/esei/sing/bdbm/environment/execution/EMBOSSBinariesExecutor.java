package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

public interface EMBOSSBinariesExecutor extends BinariesExecutor<EMBOSSBinaries> {
	public boolean checkEMBOSSBinaries(EMBOSSBinaries eBinaries);
	
	public ExecutionResult executeGetORF(
		NucleotideFasta fasta,
		NucleotideFasta orf,
		int minSize,
		int maxSize
	) throws InterruptedException, ExecutionException, IOException;
}
