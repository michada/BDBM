package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;
import java.math.BigDecimal;

import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.ei.sing.yaacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.BigDecimalOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.BooleanOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.DefaultValueBooleanOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideSearchEntry;

public class BLASTNCommand extends BDBMCommand {
	public static final FileOption OPTION_DATABASE = 
		new FileOption(
			"Database", "db", "Database from which the search entry will be retrieved",
			false, true
		);
	public static final FileOption OPTION_QUERY =
		new FileOption(
			"Query", "query", "Fasta file to be used as query", 
			false, true
		);
	public static final BigDecimalOption OPTION_EXPECTED_VALUE =
		new BigDecimalOption(
			"Expected value", "evalue", "Expected value blastn parameter", "0.05"
		);
	public static final BooleanOption OPTION_FILTER = 
		new BooleanOption(
			"Filter", "filter", "Filter results", 
			false, true 
		);
	public static final StringOption OPTION_OUTPUT_NAME = 
		new StringOption(
			"Output name", "output", "Output name", 
			false, true
		);
	public static final DefaultValueBooleanOption OPTION_KEEP_SINGLE_SEQUENCE_FILES = 
		new DefaultValueBooleanOption(
			"Keep single sequences", "keep_seqs", "Keep single sequence file", 
			false
		);
	
	public BLASTNCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "blastn";
	}
	
	@Override
	public String getDescriptiveName() {
		return "BLASTN";
	}

	@Override
	public String getDescription() {
		return "Performs a 'blastn' search";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final File database = parameters.getSingleValue(OPTION_DATABASE);
		final File query = parameters.getSingleValue(OPTION_QUERY);
		final BigDecimal expectedValue = parameters.getSingleValue(OPTION_EXPECTED_VALUE);
		final Boolean filter = parameters.getSingleValue(OPTION_FILTER);
		final String outputName = parameters.getSingleValue(OPTION_OUTPUT_NAME);
		final boolean keepSingleSequences = parameters.getSingleValue(OPTION_KEEP_SINGLE_SEQUENCE_FILES);
		
		this.controller.blastn(
			new DefaultNucleotideDatabase(database), 
			new DefaultNucleotideSearchEntry(query.getParentFile(), false).getQuery(query.getName()), 
			expectedValue, 
			filter,
			keepSingleSequences,
			outputName
		);
	}
}
