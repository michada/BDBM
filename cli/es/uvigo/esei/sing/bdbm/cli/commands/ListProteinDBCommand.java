package es.uvigo.esei.sing.bdbm.cli.commands;

import java.util.Collections;
import java.util.List;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;

public class ListProteinDBCommand extends BDBMCommand {
	public ListProteinDBCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "list_prot_db";
	}
	
	@Override
	public String getDescriptiveName() {
		return "List Protein Databases";
	}

	@Override
	public String getDescription() {
		return "Lists the BLAST protein databases";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		for (ProteinDatabase database : this.controller.listProteinDatabases()) {
			System.out.println(database.getName());
		}
	}

	@Override
	protected List<Option<?>> createOptions() {
		return Collections.emptyList();
	}
}
