package es.uvigo.esei.sing.bdbm.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import es.uvigo.ei.sing.yacli.CLIApplication;
import es.uvigo.ei.sing.yacli.Command;
import es.uvigo.esei.sing.bdbm.BDBMManager;
import es.uvigo.esei.sing.bdbm.cli.commands.BLASTNCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.BLASTPCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.GetORFCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.ImportFastaCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.ListNucleotidesDBCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.ListProteinDBCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.MakeBLASTDBCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.RetrieveSearchEntryCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.TBLASTNCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.TBLASTXCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.controller.DefaultBDBMController;
import es.uvigo.esei.sing.bdbm.environment.DefaultBDBMEnvironment;
import es.uvigo.esei.sing.bdbm.environment.execution.BinaryCheckException;
import es.uvigo.esei.sing.bdbm.persistence.DefaultBDBMRepositoryManager;

public class BDBMcli extends CLIApplication {
	private final BDBMManager manager;
	
	public BDBMcli(BDBMManager manager) {
		this.manager = manager;
	}
	
	@Override
	protected List<Command> buildCommands() throws IllegalStateException {
//		BDBMController controller = BDBM.getInstance().getController();
		/*new BDBMController(
			new DefaultBDBMRepositoryManager(BDBM.getEnvironment().getRepositoryPaths()), 
			BinaryToolFactoryBuilder.newFactory(BDBM.getEnvironment().getBlastBinaries()).createExecutor()
		);*/
		
		final BDBMController controller = this.manager.getController();
		
		return Arrays.asList(
			(Command) new ListProteinDBCommand(controller),
			(Command) new ListNucleotidesDBCommand(controller),
			(Command) new ImportFastaCommand(controller),
			(Command) new MakeBLASTDBCommand(controller),
			(Command) new RetrieveSearchEntryCommand(controller),
			(Command) new BLASTNCommand(controller),
			(Command) new BLASTPCommand(controller),
			(Command) new TBLASTNCommand(controller),
			(Command) new TBLASTXCommand(controller),
			(Command) new GetORFCommand(controller)
		);
	}

	@Override
	protected String getApplicationName() {
		return "BLAST DB Manager";
	}

	@Override
	protected String getApplicationCommand() {
		return "bdbm";
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, BinaryCheckException {
		final BDBMManager manager = new BDBMManager(
			new DefaultBDBMEnvironment(new File("bdbm.conf")),
			new DefaultBDBMRepositoryManager(),
			new DefaultBDBMController()
		);
		
		new BDBMcli(manager).run(args);
	}
}
