package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.SequenceTypeOptionConverter;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;

public class BLASTDBAliasToolCommand extends BDBMCommand {
	public static final Option<SequenceType> OPTION_DB_TYPE = 
		new Option<SequenceType>(
			"DB Type", "dbtype", "Database type: prot (proteins) or nucl (nucleotides)", 
			false, true, 
			new SequenceTypeOptionConverter()
		);
	public static final FileOption OPTION_DATABASES = 
		new FileOption(
			"Databases", "db", "Databases to be aggregated",
			false, true, true
		);
	public static final StringOption OPTION_OUTPUT = 
		new StringOption(
			"Output name", "out", "Output database name", 
			false, true
		);
	
	public BLASTDBAliasToolCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "blastdbalias";
	}
	
	@Override
	public String getDescriptiveName() {
		return "BLAST DB Alias";
	}

	@Override
	public String getDescription() {
		return "Aggegates two or more databases";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final SequenceType sequenceType = parameters.getSingleValue(OPTION_DB_TYPE);
		final List<File> databaseFiles = parameters.getAllValues(OPTION_DATABASES);
		final String outputDBName = parameters.getSingleValue(OPTION_OUTPUT);
		
		final List<Database> databases = new ArrayList<Database>(databaseFiles.size());
		
		for (File dbFile : databaseFiles) {
			databases.add(AbstractDatabase.newDatabase(sequenceType, dbFile));
		}
		
		this.controller.blastdbAliasTool(
			databases.toArray(new Database[databases.size()]),
			outputDBName
		);
	}
}
