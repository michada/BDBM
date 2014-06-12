package es.uvigo.esei.sing.bdbm.environment.binaries;

import java.io.File;
import java.util.Map;

import es.uvigo.esei.sing.bdbm.environment.NCBIEnvironmentFactory;

public class DefaultNCBIBinaries implements NCBIBinaries {
	private File baseDirectory;
	private String splign;
	private String compart;
	private String bedtools;
	
	public DefaultNCBIBinaries() {
		this(
			null, 
			defaultSplign(),
			defaultCompart(),
			defaultBedtools()
		);
	}
	
	public DefaultNCBIBinaries(File baseDirectory) {
		this(
			baseDirectory,
			new File(baseDirectory, defaultSplign()).getAbsolutePath(),
			new File(baseDirectory, defaultCompart()).getAbsolutePath(),
			new File(baseDirectory, defaultBedtools()).getAbsolutePath()
		);
	}
	
	public DefaultNCBIBinaries(String baseDirectoryPath) {
		this(new File(baseDirectoryPath));
	}
	
	public DefaultNCBIBinaries(
		File baseDirectory,
		String splign,
		String compart,
		String bedtools
	) {
		this.baseDirectory = baseDirectory;
		this.splign = splign;
		this.compart = compart;
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
			this.splign = defaultSplign();
			this.compart = defaultCompart();
			this.bedtools = defaultBedtools();
		} else {
			this.splign = new File(baseDirectory, defaultSplign()).getAbsolutePath();
			this.compart = new File(baseDirectory, defaultCompart()).getAbsolutePath();
			this.bedtools = new File(baseDirectory, defaultBedtools()).getAbsolutePath();
		}
	}

	@Override
	public String getSplign() {
		return this.splign;
	}
	
	public void setSplign(String splign) {
		this.splign = splign;
	}

	@Override
	public String getCompart() {
		return this.compart;
	}
	
	public void setCompart(String compart) {
		this.compart = compart;
	}

	@Override
	public String getBedtools() {
		return this.bedtools;
	}
	
	public void setBedtools(String bedtools) {
		this.bedtools = bedtools;
	}

	private static String defaultSplign() {
		return NCBIEnvironmentFactory.createEnvironment().getDefaultSplign();
	}
	
	private static String defaultCompart() {
		return NCBIEnvironmentFactory.createEnvironment().getDefaultCompart();
	}
	
	private static String defaultBedtools() {
		return NCBIEnvironmentFactory.createEnvironment().getDefaultBedtools();
	}
	
	public void setProperties(Map<String, String> props) {
		if (props.containsKey(NCBIBinaries.BASE_DIRECTORY_PROP)) {
			this.setBaseDirectory(props.get(NCBIBinaries.BASE_DIRECTORY_PROP));
		}
		
		if (props.containsKey(NCBIBinaries.SPLIGN_PROP)) {
			this.setSplign(props.get(NCBIBinaries.SPLIGN_PROP));
		}
		if (props.containsKey(NCBIBinaries.COMPART_PROP)) {
			this.setCompart(props.get(NCBIBinaries.COMPART_PROP));
		}
		if (props.containsKey(NCBIBinaries.BEDTOOLS_PROP)) {
			this.setBedtools(props.get(NCBIBinaries.BEDTOOLS_PROP));
		}
	}
}
