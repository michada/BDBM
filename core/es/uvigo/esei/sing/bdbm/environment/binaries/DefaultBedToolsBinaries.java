package es.uvigo.esei.sing.bdbm.environment.binaries;

import java.io.File;
import java.util.Map;

import es.uvigo.esei.sing.bdbm.environment.BedToolsEnvironmentFactory;

public class DefaultBedToolsBinaries implements BedToolsBinaries {
	private File baseDirectory;
	private String bedtools;
	
	public DefaultBedToolsBinaries() {
		this(
			null, 
			defaultBedtools()
		);
	}
	
	public DefaultBedToolsBinaries(File baseDirectory) {
		this(
			baseDirectory,
			new File(baseDirectory, defaultBedtools()).getAbsolutePath()
		);
	}
	
	public DefaultBedToolsBinaries(String baseDirectoryPath) {
		this(new File(baseDirectoryPath));
	}
	
	public DefaultBedToolsBinaries(
		File baseDirectory,
		String bedtools
	) {
		this.baseDirectory = baseDirectory;
		this.bedtools = bedtools;
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
			this.bedtools = defaultBedtools();
		} else {
			this.bedtools = new File(baseDirectory, defaultBedtools()).getAbsolutePath();
		}
	}

	@Override
	public String getBedtools() {
		return this.bedtools;
	}
	
	public void setBedtools(String bedtools) {
		this.bedtools = bedtools;
	}
	
	private static String defaultBedtools() {
		return BedToolsEnvironmentFactory.createEnvironment().getDefaultBedtools();
	}
	
	public void setProperties(Map<String, String> props) {
		if (props.containsKey(BedToolsBinaries.BASE_DIRECTORY_PROP)) {
			this.setBaseDirectory(props.get(BedToolsBinaries.BASE_DIRECTORY_PROP));
		}
		
		if (props.containsKey(BedToolsBinaries.BEDTOOLS_PROP)) {
			this.setBedtools(props.get(BedToolsBinaries.BEDTOOLS_PROP));
		}
	}
}
