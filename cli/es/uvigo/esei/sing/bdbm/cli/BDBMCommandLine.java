package es.uvigo.esei.sing.bdbm.cli;

import es.uvigo.ei.sing.yaacli.CLIApplication;
import es.uvigo.ei.sing.yaacli.CommandLine;

public class BDBMCommandLine extends CommandLine {
	@Override
	protected Class<? extends CLIApplication> getCLIApplication() {
		return BDBMcli.class;
	}
	
	public static void main(String[] args) {
		new BDBMCommandLine().run();
	}
}
