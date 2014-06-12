package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.Binaries;

public interface BinariesChecker<B extends Binaries> {
	public abstract void setBinaries(B binaries);
	public abstract void checkAll() throws BinaryCheckException;
}