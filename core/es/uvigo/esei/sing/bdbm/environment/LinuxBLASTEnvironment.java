package es.uvigo.esei.sing.bdbm.environment;

import es.uvigo.esei.sing.bdbm.environment.BLASTEnvironment;

public class LinuxBLASTEnvironment implements BLASTEnvironment {
	@Override
	public boolean isValidFor(String osName) {
		return osName.toLowerCase().contains("linux");
	}

	@Override
	public String getDefaultMakeBlastDB() {
		return "makeblastdb";
	}

	@Override
	public String getDefaultBlastDBAliasTool() {
		return "blastdb_aliastool";
	}

	@Override
	public String getDefaultBlastDBCmd() {
		return "blastdbcmd";
	}

	@Override
	public String getDefaultBlastN() {
		return "blastn";
	}
	
	@Override
	public String getDefaultBlastP() {
		return "blastp";
	}

	@Override
	public String getDefaultTBlastX() {
		return "tblastx";
	}

	@Override
	public String getDefaultTBlastN() {
		return "tblastn";
	}
}
