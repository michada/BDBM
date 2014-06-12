package es.uvigo.esei.sing.bdbm.environment.execution;

public interface ExecutionResult {
	public abstract int getExitStatus();

	public abstract String getOutput();

	public abstract String getError();
}