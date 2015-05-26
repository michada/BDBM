package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.ei.sing.yaacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.SequenceTypeOptionConverter;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractDatabase;

public class RetrieveSearchEntryCommand extends BDBMCommand {
	public static final Option<SequenceType> OPTION_DB_TYPE = 
		new Option<SequenceType>(
			"DB Type", "dbtype", "Database type: prot (proteins) or nucl (nucleotides)", 
			false, true, 
			new SequenceTypeOptionConverter()
		);
	public static final FileOption OPTION_DATABASE = 
		new FileOption(
			"Database", "db", "Database from which the search entry will be retrieved",
			false, true
		);
	public static final StringOption OPTION_ACCESSION = 
		new StringOption(
			"Accession", "accession", "Accession name", 
			false, true
		);
	
	public RetrieveSearchEntryCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "retrieve_search_entry";
	}
	
	@Override
	public String getDescriptiveName() {
		return "Retrieve Search Entry";
	}

	@Override
	public String getDescription() {
		return "Retrieves a search entry from a database";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final SequenceType sequenceType = parameters.getSingleValue(OPTION_DB_TYPE);
		final File databaseFile = parameters.getSingleValue(OPTION_DATABASE);
		final String accesionName = parameters.getSingleValue(OPTION_ACCESSION);
		
		this.controller.retrieveSearchEntry(
			AbstractDatabase.newDatabase(sequenceType, databaseFile),
			accesionName
		);
	}
}
