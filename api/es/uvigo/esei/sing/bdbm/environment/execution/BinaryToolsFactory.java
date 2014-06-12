package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.Binaries;

public interface BinaryToolsFactory<B extends Binaries, C extends BinariesChecker<B>, E extends BinariesExecutor<B>> {
	public abstract boolean isValidFor(B bBinaries);

	public abstract void setBinaries(B bBinaries)
	throws BinaryCheckException;

	public abstract C createChecker();

	public abstract E createExecutor();
}