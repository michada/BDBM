package es.uvigo.esei.sing.bdbm.environment.binaries;

import java.io.File;
import java.util.Map;

import es.uvigo.esei.sing.bdbm.environment.SplignEnvironmentFactory;

public class DefaultSplignBinaries implements SplignBinaries {
	private File baseDirectory;
	private String splign;
	
	public DefaultSplignBinaries() {
		this(null, defaultSplign());
	}
	
	public DefaultSplignBinaries(File baseDirectory) {
		this(
			baseDirectory,
			new File(baseDirectory, defaultSplign()).getAbsolutePath()
		);
	}
	
	public DefaultSplignBinaries(String baseDirectoryPath) {
		this(new File(baseDirectoryPath));
	}
	
	public DefaultSplignBinaries(
		File baseDirectory,
		String splign
	) {
		this.baseDirectory = baseDirectory;
		this.splign = splign;
	}

	@Override
	public File getBaseDirectory() {
		return this.baseDirectory;
	}
	
	public void setBaseDirectory(String path) {
		this.setBaseDirectory(path.isEmpty() ? null : new File(path));
	}
	
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		
		if (this.baseDirectory == null) {
			this.splign = defaultSplign();
		} else {
			this.splign = new File(baseDirectory, defaultSplign()).getAbsolutePath();
		}
	}

	@Override
	public String getSplign() {
		return this.splign;
	}
	
	public void setSplign(String splign) {
		this.splign = splign;
	}

	private static String defaultSplign() {
		return SplignEnvironmentFactory.createEnvironment().getDefaultSplign();
	}
	
	public void setProperties(Map<String, String> props) {
		if (props.containsKey(SplignBinaries.BASE_DIRECTORY_PROP)) {
			this.setBaseDirectory(props.get(SplignBinaries.BASE_DIRECTORY_PROP));
		}
		
		if (props.containsKey(SplignBinaries.SPLIGN_PROP)) {
			this.setSplign(props.get(SplignBinaries.SPLIGN_PROP));
		}
	}
}
