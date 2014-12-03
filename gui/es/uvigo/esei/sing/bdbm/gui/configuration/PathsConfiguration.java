package es.uvigo.esei.sing.bdbm.gui.configuration;

import java.io.File;

public class PathsConfiguration {
	private final File baseRespository;
	private final File baseBLAST;
	private final File baseEMBOSS;
	private final File baseBedTools;
	private final File baseSplign;
	private final File baseCompart;
	
	public PathsConfiguration(
		File baseRespository, File baseBLAST, File baseEMBOSS,
		File baseBedTools, File baseSplign, File baseCompart
	) {
		this.baseRespository = baseRespository;
		this.baseBLAST = baseBLAST;
		this.baseEMBOSS = baseEMBOSS;
		this.baseBedTools = baseBedTools;
		this.baseSplign = baseSplign;
		this.baseCompart = baseCompart;
	}
	
	public File getBaseRespository() {
		return baseRespository;
	}
	
	public File getBaseBLAST() {
		return baseBLAST;
	}
	
	public File getBaseEMBOSS() {
		return baseEMBOSS;
	}
	
	public File getBaseBedTools() {
		return baseBedTools;
	}
	
	public File getBaseSplign() {
		return baseSplign;
	}
	
	public File getBaseCompart() {
		return baseCompart;
	}
}