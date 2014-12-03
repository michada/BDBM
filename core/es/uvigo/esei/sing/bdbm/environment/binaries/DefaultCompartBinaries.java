package es.uvigo.esei.sing.bdbm.environment.binaries;

import java.io.File;
import java.util.Map;

import es.uvigo.esei.sing.bdbm.environment.CompartEnvironmentFactory;

public class DefaultCompartBinaries implements CompartBinaries {
	private File baseDirectory;
	private String compart;
	
	public DefaultCompartBinaries() {
		this(null, defaultCompart());
	}
	
	public DefaultCompartBinaries(File baseDirectory) {
		this(
			baseDirectory,
			new File(baseDirectory, defaultCompart()).getAbsolutePath()
		);
	}
	
	public DefaultCompartBinaries(String baseDirectoryPath) {
		this(new File(baseDirectoryPath));
	}
	
	public DefaultCompartBinaries(
		File baseDirectory,
		String compart
	) {
		this.baseDirectory = baseDirectory;
		this.compart = compart;
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
			this.compart = defaultCompart();
		} else {
			this.compart = new File(baseDirectory, defaultCompart()).getAbsolutePath();
		}
	}

	@Override
	public String getCompart() {
		return this.compart;
	}
	
	public void setCompart(String compart) {
		this.compart = compart;
	}
	
	private static String defaultCompart() {
		return CompartEnvironmentFactory.createEnvironment().getDefaultCompart();
	}
	
	public void setProperties(Map<String, String> props) {
		if (props.containsKey(CompartBinaries.BASE_DIRECTORY_PROP)) {
			this.setBaseDirectory(props.get(CompartBinaries.BASE_DIRECTORY_PROP));
		}
		
		if (props.containsKey(CompartBinaries.COMPART_PROP)) {
			this.setCompart(props.get(CompartBinaries.COMPART_PROP));
		}
	}
}
