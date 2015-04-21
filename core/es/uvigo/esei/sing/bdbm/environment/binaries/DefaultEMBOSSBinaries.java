package es.uvigo.esei.sing.bdbm.environment.binaries;

import java.io.File;
import java.util.Map;

import es.uvigo.esei.sing.bdbm.environment.EMBOSSEnvironmentFactory;

public class DefaultEMBOSSBinaries implements EMBOSSBinaries {
	private File baseDirectory;
	private String getORF;
	private String revseq;
	
	public DefaultEMBOSSBinaries() {
		this(
			null,
			defaultGetORF(),
			defaultRevseq()
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
				baseDirectory.toPath().resolve(defaultGetORF()).toString(),
			baseDirectory == null ?
				defaultRevseq() :
				baseDirectory.toPath().resolve(defaultRevseq()).toString()
		);
	}
	
	public DefaultEMBOSSBinaries(
		File baseDirectory, String getORF, String revseq
	) {
		this.baseDirectory = baseDirectory;
		this.getORF = getORF;
		this.revseq = revseq;
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
	
	public void setRevseq(String revseq) {
		this.revseq = revseq;
	}

	@Override
	public String getRevseq() {
		return this.revseq;
	}
	
	private static String defaultGetORF() {
		return EMBOSSEnvironmentFactory.createEnvironment().getDefaultGetORF();
	}
	
	private static String defaultRevseq() {
		return EMBOSSEnvironmentFactory.createEnvironment().getDefaultRevseq();
	}
	
	public void setProperties(Map<String, String> props) {
		if (props.containsKey(EMBOSSBinaries.BASE_DIRECTORY_PROP)) {
			this.setBaseDirectory(props.get(EMBOSSBinaries.BASE_DIRECTORY_PROP));
		}
		
		if (props.containsKey(EMBOSSBinaries.GETORF_PROP)) {
			this.setGetORF(props.get(EMBOSSBinaries.GETORF_PROP));
		}
		
		if (props.containsKey(EMBOSSBinaries.REVSEQ_PROP)) {
			this.setRevseq(props.get(EMBOSSBinaries.REVSEQ_PROP));
		}
	}
}
