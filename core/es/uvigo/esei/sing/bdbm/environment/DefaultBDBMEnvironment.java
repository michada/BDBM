package es.uvigo.esei.sing.bdbm.environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultBLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultEMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultNCBIBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;
import es.uvigo.esei.sing.bdbm.environment.paths.DefaultRepositoryPaths;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public class DefaultBDBMEnvironment implements BDBMEnvironment {
	private final DefaultRepositoryPaths repositoryPaths;
	private final DefaultBLASTBinaries blastBinaries;
	private final DefaultEMBOSSBinaries embossBinaries;
	private final DefaultNCBIBinaries ncbiBinaries;
	
	private final File propertiesFile;
	private final Properties defaultProperties;
	
	public DefaultBDBMEnvironment() {
		this.blastBinaries = this.createBLASTBinaries(null);
		this.embossBinaries = this.createEMBOSSBinaries(null);
		this.ncbiBinaries = this.createNCBIBinaries(null);
		this.repositoryPaths = new DefaultRepositoryPaths(new File("."));
		
		this.propertiesFile = null;
		this.defaultProperties = new Properties();
	}
	
	public DefaultBDBMEnvironment(File propertiesFile)
	throws FileNotFoundException, IOException, IllegalStateException {
		this.propertiesFile = propertiesFile;
		this.defaultProperties = new Properties();
		
		if (this.propertiesFile != null) {
			this.defaultProperties.load(new FileReader(this.propertiesFile));
		}
		
		for (String property : new String[] {
			RepositoryPaths.BASE_DIRECTORY_PROP,
			BLASTBinaries.BASE_DIRECTORY_PROP,
			EMBOSSBinaries.BASE_DIRECTORY_PROP,
			NCBIBinaries.BASE_DIRECTORY_PROP
		}) {
			if (!this.hasProperty(property)) {
				throw new IllegalStateException(
					"Missing property in configuration file: " + property
				);
			}
		}
		
		this.repositoryPaths = new DefaultRepositoryPaths(
			new File(this.getProperty(RepositoryPaths.BASE_DIRECTORY_PROP))
		);
		this.blastBinaries = new DefaultBLASTBinaries(
			this.getProperty(BLASTBinaries.BASE_DIRECTORY_PROP)
		);
		this.embossBinaries = new DefaultEMBOSSBinaries(
			this.getProperty(EMBOSSBinaries.BASE_DIRECTORY_PROP)
		);
		this.ncbiBinaries = new DefaultNCBIBinaries(
			this.getProperty(NCBIBinaries.BASE_DIRECTORY_PROP)
		);
	}
	
	private boolean changeProperty(String propertyName, String propertyValue, boolean persist) throws IOException {
		if (propertyValue == null)
			throw new IllegalArgumentException("New value can't be null");
		
		final String currentValue = this.getProperty(propertyName);
		
		if (currentValue == null || !currentValue.equals(propertyValue)) {
			this.defaultProperties.put(propertyName, propertyValue);
			
			if (persist) {
				saveToProperties();
			}
			
			final Map<String, String> propertyMap = 
				Collections.singletonMap(propertyName, propertyValue);
			
			this.blastBinaries.setProperties(propertyMap);
			this.embossBinaries.setProperties(propertyMap);
			this.ncbiBinaries.setProperties(propertyMap);
			this.repositoryPaths.setProperties(propertyMap);
			
			return true;
		} else {
			return false;
		}
	}

	public void saveToProperties() throws IOException {
		this.defaultProperties.store(
			new FileWriter(this.propertiesFile), 
			"Configuration modified by BDBM"
		);
	}
	
	@Override
	public String getProperty(String propertyName) {
		String propertyValue = System.getProperty(propertyName);
		
		if (propertyValue == null)
			propertyValue = this.defaultProperties.getProperty(propertyName);
		
		return propertyValue;
	}
	
	@Override
	public boolean hasProperty(String propertyName) {
		return System.getProperty(propertyName) != null ||
			this.defaultProperties.containsKey(propertyName);
	}

	@Override
	public BLASTBinaries getBLASTBinaries() {
		return this.blastBinaries;
	}
	
	@Override
	public EMBOSSBinaries getEMBOSSBinaries() {
		return this.embossBinaries;
	}

	@Override
	public NCBIBinaries getNCBIBinaries() {
		return this.ncbiBinaries;
	}

	@Override
	public RepositoryPaths getRepositoryPaths() {
		return this.repositoryPaths;
	}
	
	public boolean initializeRepositoryPaths() throws IOException {
		if (this.repositoryPaths.checkBaseDirectory(this.repositoryPaths.getBaseDirectory())) {
			return false;	// Already exists
		} else {
			this.repositoryPaths.buildBaseDirectory(this.repositoryPaths.getBaseDirectory());
			return true;	// Created
		}
	}

	@Override
	public boolean changeRepositoryPath(File repositoryPath) throws IOException {
		return this.changeRepositoryPath(repositoryPath, true);
	}

	@Override
	public boolean changeRepositoryPath(File repositoryPath, boolean persist)
			throws IOException {
		if (this.changeProperty(
			RepositoryPaths.BASE_DIRECTORY_PROP, 
			repositoryPath.getAbsolutePath(),
			persist
		)) {
			this.repositoryPaths.setBaseDirectory(repositoryPath);
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean changeBLASTPath(File blastPath) throws IOException {
		return this.changeBLASTPath(blastPath, true);
	}

	@Override
	public boolean changeBLASTPath(File blastPath, boolean persist)
	throws IOException {
		if (this.changeProperty(
			BLASTBinaries.BASE_DIRECTORY_PROP, 
			blastPath.getAbsolutePath(),
			persist
		)) {
			this.blastBinaries.setBaseDirectory(blastPath);
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean changeEMBOSSPath(File embossPath) throws IOException {
		return this.changeEMBOSSPath(embossPath, true);
	}

	@Override
	public boolean changeEMBOSSPath(File embossPath, boolean persist) throws IOException {
		if (this.changeProperty(
			EMBOSSBinaries.BASE_DIRECTORY_PROP,
			embossPath.getAbsolutePath(),
			persist
		)) {
			this.embossBinaries.setBaseDirectory(embossPath);
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean changeNCBIPath(File blastPath) throws IOException {
		return this.changeNCBIPath(blastPath, true);
	}

	@Override
	public boolean changeNCBIPath(File ncbiPath, boolean persist)
	throws IOException {
		if (this.changeProperty(
			NCBIBinaries.BASE_DIRECTORY_PROP,
			ncbiPath.getAbsolutePath(),
			persist
		)) {
			this.ncbiBinaries.setBaseDirectory(ncbiPath);
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean changePaths(File repositoryPath, File blastPath, File embossPath, File ncbiPath) 
	throws IOException {
		return this.changeProperty(RepositoryPaths.BASE_DIRECTORY_PROP, repositoryPath.getAbsolutePath(), true)
				|| this.changeProperty(BLASTBinaries.BASE_DIRECTORY_PROP, blastPath == null ? "" : blastPath.getAbsolutePath(), true)
				|| this.changeProperty(EMBOSSBinaries.BASE_DIRECTORY_PROP, embossPath == null ? "" : embossPath.getAbsolutePath(), true)
				|| this.changeProperty(NCBIBinaries.BASE_DIRECTORY_PROP, ncbiPath == null ? "" : ncbiPath.getAbsolutePath(), true);
	}

	@Override
	public boolean isAccessionInferEnabled() {
		return Boolean.parseBoolean(this.getProperty("retrievesearchentry.accession.infer"));
	}

	@Override
	public DefaultBLASTBinaries createBLASTBinaries(String path) {
		return new DefaultBLASTBinaries(path);
	}

	@Override
	public DefaultEMBOSSBinaries createEMBOSSBinaries(String path) {
		return new DefaultEMBOSSBinaries(path);
	}

	@Override
	public DefaultNCBIBinaries createNCBIBinaries(String path) {
		return new DefaultNCBIBinaries(path);
	}
}
