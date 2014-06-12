package es.uvigo.esei.sing.bdbm.persistence;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public interface Repository {
	public abstract void setRepositoryPaths(RepositoryPaths repositoryPaths)
	throws IOException;
	
	public abstract void shutdown();
}
