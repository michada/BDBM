package es.uvigo.esei.sing.bdbm.environment;

import es.uvigo.esei.sing.bdbm.environment.BLASTEnvironment;

public class WindowsBLASTEnvironment implements BLASTEnvironment {
	@Override
	public boolean isValidFor(String osName) {
		return osName.toLowerCase().contains("windows");
	}

	@Override
	public String getDefaultMakeBlastDB() {
		return "makeblastdb.exe";
	}

	@Override
	public String getDefaultBlastDBAliasTool() {
		return "blastdb_aliastool.exe";
	}

	@Override
	public String getDefaultBlastDBCmd() {
		return "blastdbcmd.exe";
	}

	@Override
	public String getDefaultBlastN() {
		return "blastn.exe";
	}

	@Override
	public String getDefaultBlastP() {
		return "blastp.exe";
	}

	@Override
	public String getDefaultTBlastX() {
		return "tblastx.exe";
	}

	@Override
	public String getDefaultTBlastN() {
		return "tblastn.exe";
	}
}
