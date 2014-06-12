package es.uvigo.esei.sing.bdbm.environment;

import java.io.File;
import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public interface BDBMEnvironment {
	public abstract RepositoryPaths getRepositoryPaths();
	public abstract BLASTBinaries getBlastBinaries();
	public abstract EMBOSSBinaries getEmbossBinaries();
	
	public abstract boolean isAccessionInferEnabled();
	
	public abstract String getProperty(String propertyName);
	public abstract boolean hasProperty(String propertyName);
	
	public abstract boolean changeEmbossPath(File embossPath)
	throws IOException;
	public abstract boolean changeBlastPath(File blastPath)
	throws IOException;
	public abstract boolean changeRepositoryPath(File repositoryPath)
	throws IOException;
	public abstract boolean changePaths(File repositoryPath, File blastPath, File embossPath)
	throws IOException;
	
	public abstract boolean changeEmbossPath(File embossPath, boolean persist)
	throws IOException;
	public abstract boolean changeBlastPath(File blastPath, boolean persist)
	throws IOException;
	public abstract boolean changeRepositoryPath(File repositoryPath, boolean persist)
	throws IOException;
	
	// Move to another class?
	public abstract BLASTBinaries createBLASTBinaries(String path);
	public abstract EMBOSSBinaries createEMBOSSBinaries(String path);
}
