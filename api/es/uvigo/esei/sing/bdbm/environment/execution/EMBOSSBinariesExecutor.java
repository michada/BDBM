package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

public interface EMBOSSBinariesExecutor extends BinariesExecutor<EMBOSSBinaries> {
	public boolean checkEMBOSSBinaries(EMBOSSBinaries eBinaries);
	
	public ExecutionResult executeGetORF(
		NucleotideFasta fasta,
		NucleotideFasta orf,
		int minSize,
		int maxSize
	) throws InterruptedException, ExecutionException;
	
	public ExecutionResult executeRevseq(
		NucleotideFasta fasta,
		NucleotideFasta outputFasta
	) throws InterruptedException, ExecutionException;
}
