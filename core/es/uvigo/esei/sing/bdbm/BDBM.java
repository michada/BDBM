package es.uvigo.esei.sing.bdbm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.controller.DefaultBDBMController;
import es.uvigo.esei.sing.bdbm.environment.BDBMEnvironment;
import es.uvigo.esei.sing.bdbm.environment.DefaultBDBMEnvironment;
import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.BinaryCheckException;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.NCBIBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.NCBIBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;
import es.uvigo.esei.sing.bdbm.persistence.BDBMRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.DefaultBDBMRepositoryManager;

@Deprecated
public class BDBM extends Observable {
	private final static Logger LOG = LoggerFactory.getLogger(BDBM.class);
	
	private static volatile BDBM INSTANCE;
	
	private BDBMEnvironment environment;
	private BDBMRepositoryManager repositoryManager;
	private BDBMController controller;
	
	private Properties systemProperties;
	private Properties defaultProperties;
	
	public static BDBM getInstance() throws IllegalStateException {
		if (INSTANCE == null) {
			synchronized (BDBM.class) {
				if (INSTANCE == null) {
					INSTANCE = new BDBM();
				}
			}
		}
		
		return INSTANCE;
	}
	
	public BDBM() throws IllegalStateException {
		this.initEnvironment();
	}
	
	// MOVED TO BDBMEnvironment
	public String getProperty(String propertyName) {
		if (this.systemProperties.containsKey(propertyName)) {
			return this.systemProperties.getProperty(propertyName);
		} else if (this.defaultProperties != null) {
			return this.defaultProperties.getProperty(propertyName);
		} else{
			return null;
		}
	}
	
	public boolean hasProperty(String propertyName) {
		return this.systemProperties.containsKey(propertyName) ||
			this.defaultProperties.containsKey(propertyName);
	}
	// End MOVED TO BDBMEnvironment
	
//	private final static Map<String, String> propertiesToMap(Properties ... properties) {
//		final List<Properties> reverseProperties = Arrays.asList(properties);
//		Collections.reverse(reverseProperties);
//		
//		final Map<String, String> map = new HashMap<String, String>();
//		
//		for (Properties props : reverseProperties) {
//			for (String propName : props.stringPropertyNames()) {
//				map.put(propName, props.getProperty(propName));
//			}
//		}
//		
//		return map;
//	}

	protected void initEnvironment() throws IllegalStateException {
		this.systemProperties = System.getProperties();
		
		final File defaultConfFile = this.getDefaultConfigurationFile();
		
		if (defaultConfFile.canRead()) {
			this.defaultProperties = new Properties();
			try {
				this.defaultProperties.load(new FileReader(defaultConfFile));
				
				this.environment = new DefaultBDBMEnvironment(defaultConfFile);
				
				LOG.info("Environment configured with file properties");
			} catch (Exception e) {
				this.environment = new DefaultBDBMEnvironment();
				
				LOG.error("Error loading default configuration. Environment configured without file properties", e);
			}
		} else {
			this.environment = new DefaultBDBMEnvironment();
			LOG.warn("Environment configured without file properties");
		}
		
		try {
			this.repositoryManager = new DefaultBDBMRepositoryManager(
				this.getEnvironment().getRepositoryPaths()
			);
			
			final BLASTBinaryToolsFactory blastFactory;
			final EMBOSSBinaryToolsFactory embossFactory;
			final NCBIBinaryToolsFactory ncbiFactory;
			
			try {
				blastFactory = BLASTBinaryToolsFactoryBuilder.newFactory(
					this.getEnvironment().getBLASTBinaries()
				);
			} catch (BinaryCheckException bce) {
				throw new IllegalStateException("Invalid BLAST binaries", bce);
			}
			
			try {
				embossFactory = EMBOSSBinaryToolsFactoryBuilder.newFactory(
					this.getEnvironment().getEMBOSSBinaries()
				);
			} catch (BinaryCheckException bce) {
				throw new IllegalStateException("Invalid EMBOSS binaries", bce);
			}
			
			try {
				ncbiFactory = NCBIBinaryToolsFactoryBuilder.newFactory(
					this.getEnvironment().getNCBIBinaries()
				);
			} catch (BinaryCheckException bce) {
				throw new IllegalStateException("Invalid NCBI binaries", bce);
			}
			
			this.controller = new DefaultBDBMController(
				this.getRepositoryManager(),
				blastFactory.createExecutor(),
				embossFactory.createExecutor(),
				ncbiFactory.createExecutor()
			);
			
			final RepositoryPaths repositoryPaths = this.environment.getRepositoryPaths();
			if (!repositoryPaths.checkBaseDirectory(repositoryPaths.getBaseDirectory())) {
				try {
					repositoryPaths.buildBaseDirectory(repositoryPaths.getBaseDirectory());
				} catch (IOException e) {
					throw new IllegalStateException("Repository could not be created");
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error creating controller", e);
		}
	}

	public boolean changeRepositoryPath(File repositoryPath) throws IOException {
		if (this.changeProperty(
			RepositoryPaths.BASE_DIRECTORY_PROP, 
			repositoryPath.getAbsolutePath()
		)) {
			this.initEnvironment();
			
			this.setChanged();
			this.notifyObservers(RepositoryPaths.BASE_DIRECTORY_PROP);
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean changeBlastPath(File blastPath) throws IOException {
		if (this.changeProperty(
			BLASTBinaries.BASE_DIRECTORY_PROP, 
			blastPath.getAbsolutePath()
		)) {
			this.initEnvironment();
			
			this.setChanged();
			this.notifyObservers(BLASTBinaries.BASE_DIRECTORY_PROP);
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean changePaths(
		File repositoryPath, 
		File blastPath,
		File embossPath
	) throws IOException {
		final List<String> changedProperties = new ArrayList<String>(2);

		if (this.changeProperty(
			RepositoryPaths.BASE_DIRECTORY_PROP, 
			repositoryPath.getAbsolutePath()
		)) {
			changedProperties.add(RepositoryPaths.BASE_DIRECTORY_PROP);
		}
		if (this.changeProperty(
			BLASTBinaries.BASE_DIRECTORY_PROP,	
			blastPath == null ? "" : blastPath.getAbsolutePath())
		) {
			changedProperties.add(BLASTBinaries.BASE_DIRECTORY_PROP);
		}
		if (this.changeProperty(
			EMBOSSBinaries.BASE_DIRECTORY_PROP,	
			embossPath == null ? "" : embossPath.getAbsolutePath())
		) {
			changedProperties.add(EMBOSSBinaries.BASE_DIRECTORY_PROP);
		}
		
		if (!changedProperties.isEmpty()) {
			this.initEnvironment();
			
			this.setChanged();
			this.notifyObservers(changedProperties);
			
			return true;
		} else {
			return false;
		}
	}

	protected boolean changeProperty(String property, String newValue)
	throws IOException, FileNotFoundException, IllegalArgumentException {
		if (newValue == null)
			throw new IllegalArgumentException("New value can't be null");
		
		final File defaultConfFile = this.getDefaultConfigurationFile();
		final Properties props = new Properties();
		props.load(new FileReader(defaultConfFile));

		final String currentValue = props.getProperty(property);
		
		if (currentValue == null || !currentValue.equals(newValue)) {
			props.put(property, newValue);
			
			props.store(new FileWriter(defaultConfFile), "Configuration modified by BDBM");
			
			return true;
		} else {
			return false;
		}
	}
	
	protected File getDefaultConfigurationFile() {
		return new File(
			System.getProperties().getProperty("bdbm.conf", "bdbm.conf")
		);
	}
	
	public BDBMEnvironment getEnvironment() {
		return this.environment;
	}

	public BDBMRepositoryManager getRepositoryManager() {
		return this.repositoryManager;
	}
	
	public BDBMController getController() {
		return this.controller;
	}
}