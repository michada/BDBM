package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.ei.sing.yaacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.SequenceTypeOptionConverter;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractFasta;

public class MakeBLASTDBCommand extends BDBMCommand {
	public static final Option<SequenceType> OPTION_DB_TYPE = 
		new Option<SequenceType>(
			"DB Type", "dbtype", "Database type: prot (proteins) or nucl (nucleotides)", 
			false, true, 
			new SequenceTypeOptionConverter()
		);
	public static final FileOption OPTION_INPUT = 
		new FileOption(
			"Input fasta", "in", "Input FASTA file", 
			false, true
		);
	public static final StringOption OPTION_OUTPUT = 
		new StringOption(
			"Output name", "out", "Output database name", 
			false, true
		);
	
	public MakeBLASTDBCommand(BDBMController controller) {
		super(controller);
	}
	
	@Override
	public String getName() {
		return "make_blast_db";
	}
	
	@Override
	public String getDescriptiveName() {
		return "Make BLAST Database";
	}

	@Override
	public String getDescription() {
		return "Creates a BLAST database from a FASTA file";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final SequenceType dbType = parameters.getSingleValue(OPTION_DB_TYPE);
		final File input = parameters.getSingleValue(OPTION_INPUT);
		final String outputDBName = parameters.getSingleValue(OPTION_OUTPUT);
		
		this.controller.makeBlastDB(AbstractFasta.newFasta(dbType, input), outputDBName);
	}
}
