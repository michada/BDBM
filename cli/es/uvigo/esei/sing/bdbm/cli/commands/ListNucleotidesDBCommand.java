package es.uvigo.esei.sing.bdbm.cli.commands;

import java.util.Collections;
import java.util.List;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;

public class ListNucleotidesDBCommand extends BDBMCommand {
	public ListNucleotidesDBCommand(BDBMController controller) {
		super(controller);
	}
	
	@Override
	public String getName() {
		return "list_nucl_db";
	}
	
	@Override
	public String getDescriptiveName() {
		return "List Nucleotide Databases";
	}

	@Override
	public String getDescription() {
		return "Lists the BLAST nucleotide databases";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		for (NucleotideDatabase database : this.controller.listNucleotideDatabases()) {
			System.out.println(database.getName());
		}
	}

	@Override
	protected List<Option<?>> createOptions() {
		return Collections.emptyList();
	}
}
