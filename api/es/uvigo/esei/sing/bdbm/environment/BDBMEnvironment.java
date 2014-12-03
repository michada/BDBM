package es.uvigo.esei.sing.bdbm.environment;

import java.io.File;
import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.BedToolsBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.CompartBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.SplignBinaries;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public interface BDBMEnvironment {
	public abstract RepositoryPaths getRepositoryPaths();
	public abstract BLASTBinaries getBLASTBinaries();
	public abstract EMBOSSBinaries getEMBOSSBinaries();
	public abstract BedToolsBinaries getBedToolsBinaries();
	public abstract SplignBinaries getSplignBinaries();
	public abstract CompartBinaries getCompartBinaries();
	
	public abstract boolean isAccessionInferEnabled();
	
	public abstract String getProperty(String propertyName);
	public abstract boolean hasProperty(String propertyName);
	
	public abstract boolean changeEMBOSSPath(File embossPath)
	throws IOException;
	public abstract boolean changeBLASTPath(File blastPath)
	throws IOException;
	public abstract boolean changeBedToolsPath(File bedToolsPath)
	throws IOException;
	public abstract boolean changeSplignPath(File splignPath)
	throws IOException;
	public abstract boolean changeCompartPath(File compartPath)
	throws IOException;
	public abstract boolean changeRepositoryPath(File repositoryPath)
	throws IOException;
	public abstract boolean changePaths(
		File repositoryPath, File blastPath, File embossPath,
		File bedToolsPath, File splignPath, File compartPath
	) throws IOException;
	
	public abstract boolean changeEMBOSSPath(File embossPath, boolean persist)
	throws IOException;
	public abstract boolean changeBLASTPath(File blastPath, boolean persist)
	throws IOException;
	public abstract boolean changeBedToolsPath(File bedToolsPath, boolean persist)
	throws IOException;
	public abstract boolean changeSplignPath(File splignPath, boolean persist)
	throws IOException;
	public abstract boolean changeCompartPath(File compartPath, boolean persist)
	throws IOException;
	public abstract boolean changeRepositoryPath(File repositoryPath, boolean persist)
	throws IOException;
	
	// Move to another class?
	public abstract BLASTBinaries createBLASTBinaries(String path);
	public abstract EMBOSSBinaries createEMBOSSBinaries(String path);
	public abstract BedToolsBinaries createBedToolsBinaries(String path);
	public abstract SplignBinaries createSplignBinaries(String path);
	public abstract CompartBinaries createCompartBinaries(String path);
}
