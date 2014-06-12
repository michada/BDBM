package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.Binaries;

public interface BinariesExecutor<B extends Binaries> {
	public abstract void setBinaries(B binaries)
	throws BinaryCheckException;
}