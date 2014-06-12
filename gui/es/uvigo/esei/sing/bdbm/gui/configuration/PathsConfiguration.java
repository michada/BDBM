package es.uvigo.esei.sing.bdbm.gui.configuration;

import java.io.File;

public class PathsConfiguration {
	private final File baseRespository;
	private final File baseBLAST;
	private final File baseEMBOSS;
	
	public PathsConfiguration(File baseRespository, File baseBLAST, File baseEMBOSS) {
		this.baseRespository = baseRespository;
		this.baseBLAST = baseBLAST;
		this.baseEMBOSS = baseEMBOSS;
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
}