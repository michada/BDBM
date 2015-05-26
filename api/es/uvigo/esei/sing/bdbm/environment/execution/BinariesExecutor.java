package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.Binaries;

public interface BinariesExecutor<B extends Binaries> {
	public abstract void setBinaries(B binaries)
	throws BinaryCheckException;
	
	public static interface InputLineCallback {
		public void info(String message);
		public void line(String line);
		public void error(String message, Exception e);
		public void inputStarted();
		public void inputFinished();
	}
}