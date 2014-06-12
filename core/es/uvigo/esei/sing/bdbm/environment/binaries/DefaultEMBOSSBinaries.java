package es.uvigo.esei.sing.bdbm.environment.binaries;

import java.io.File;
import java.util.Map;

import es.uvigo.esei.sing.bdbm.environment.EMBOSSEnvironmentFactory;
import es.uvigo.esei.sing.bdbm.environment.binaries.EMBOSSBinaries;

public class DefaultEMBOSSBinaries implements EMBOSSBinaries {
	private File baseDirectory;
	private String getORF;
	
	public DefaultEMBOSSBinaries() {
		this(
			null,
			defaultGetORF()
		);
	}
	
	public DefaultEMBOSSBinaries(String baseDirectory) {
		this(baseDirectory == null ? null : new File(baseDirectory));
	}
	
	public DefaultEMBOSSBinaries(File baseDirectory) {
		this(
			baseDirectory,
			baseDirectory == null ? 
				defaultGetORF() : 
				new File(baseDirectory, defaultGetORF()).getAbsolutePath()
		);
	}
	
	public DefaultEMBOSSBinaries(
		File baseDirectory, String getORF
	) {
		this.baseDirectory = baseDirectory;
		this.getORF = getORF;
	}
	
	public void setBaseDirectory(String path) {
		this.setBaseDirectory(path.isEmpty() ? null : new File(path));
	}
	
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		
		if (this.baseDirectory == null) {
			this.getORF = defaultGetORF();
		} else {
			this.getORF = new File(baseDirectory, defaultGetORF()).getAbsolutePath();
		}
	}

	@Override
	public File getBaseDirectory() {
		return this.baseDirectory;
	}
	
	public void setGetORF(String getORF) {
		this.getORF = getORF;
	}

	@Override
	public String getGetORF() {
		return this.getORF;
	}
	
	private static String defaultGetORF() {
		return EMBOSSEnvironmentFactory.createEnvironment().getDefaultGetORF();
	}
	
	public void setProperties(Map<String, String> props) {
		if (props.containsKey(EMBOSSBinaries.BASE_DIRECTORY_PROP)) {
			this.setBaseDirectory(props.get(EMBOSSBinaries.BASE_DIRECTORY_PROP));
		}
		
		if (props.containsKey(EMBOSSBinaries.GETORF_PROP)) {
			this.setGetORF(props.get(EMBOSSBinaries.GETORF_PROP));
		}
	}
}
