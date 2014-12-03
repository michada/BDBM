package es.uvigo.esei.sing.bdbm.environment.execution;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.environment.binaries.BedToolsBinaries;

public class DefaultBedToolsBinariesExecutor
extends AbstractBinariesExecutor<BedToolsBinaries>
implements BedToolsBinariesExecutor {
	private final static Logger LOG = LoggerFactory.getLogger(BedToolsBinariesExecutor.class);
	
	public DefaultBedToolsBinariesExecutor() {}

	public DefaultBedToolsBinariesExecutor(BedToolsBinaries nBinaries)
	throws BinaryCheckException {
		this.setBinaries(nBinaries);
	}

	@Override
	public void setBinaries(
		BedToolsBinaries nBinaries
	) throws BinaryCheckException {
		DefaultBedToolsBinariesChecker.checkAll(nBinaries);
		
		this.binaries = nBinaries;
	}
	
	@Override
	public boolean checkBedToolsBinaries(BedToolsBinaries bBinaries) {
		try {
			DefaultBedToolsBinariesChecker.checkAll(bBinaries);
			
			return true;
		} catch (BinaryCheckException bce) {
			return false;
		}
	}
	
	@Override
	public ExecutionResult getfasta(
		String fi, String bed, String fo, InputLineCallback ... callbacks
	) throws ExecutionException, InterruptedException {
		return AbstractBinariesExecutor.executeCommand(
			LOG,
			false,
			Arrays.asList(callbacks),
			this.binaries.getBedtools(),
			"getfasta", 
			"-fi", fi,
			"-bed", bed,
			"-fo", fo,
			"-name"
		);
	}
}
