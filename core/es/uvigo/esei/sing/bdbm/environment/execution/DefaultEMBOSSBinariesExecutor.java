package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

public class DefaultEMBOSSBinariesExecutor 
extends AbstractBinariesExecutor<EMBOSSBinaries> 
implements EMBOSSBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultEMBOSSBinariesExecutor.class);
	
	public DefaultEMBOSSBinariesExecutor() {}

	public DefaultEMBOSSBinariesExecutor(EMBOSSBinaries eBinaries)
	throws BinaryCheckException {
		this.setBinaries(eBinaries);
	}
	
	@Override
	public void setBinaries(EMBOSSBinaries binaries)
	throws BinaryCheckException {
		DefaultEMBOSSBinariesChecker.checkAll(binaries);
		
		super.setBinaries(binaries);
	}

	@Override
	public boolean checkEMBOSSBinaries(EMBOSSBinaries eBinaries) {
		try {
			DefaultEMBOSSBinariesChecker.checkAll(eBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}

	@Override
	public ExecutionResult executeGetORF(
		NucleotideFasta fasta, 
		NucleotideFasta orf,
		int minSize,
		int maxSize
	) throws InterruptedException, ExecutionException, IOException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			this.binaries.getGetORF(), 
			"-snucleotide", fasta.getFile().getAbsolutePath(),
			"-outseq", orf.getFile().getAbsolutePath(),
			"-table", "0",
			"-minsize", Integer.toString(minSize),
			"-maxsize", Integer.toString(maxSize),
			"-find", "3"
		);
	}

	@Override
	public ExecutionResult executeRevseq(
		NucleotideFasta fasta,
		NucleotideFasta outputFasta
	) throws InterruptedException, ExecutionException, IOException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			this.binaries.getGetORF(), 
			"-sequence", fasta.getFile().getAbsolutePath(),
			"-outseq", outputFasta.getFile().getAbsolutePath()
		);
	}
}
