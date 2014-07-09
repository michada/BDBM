package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideFasta;

public class SplignCompartCommand extends BDBMCommand {
	public static final FileOption OPTION_REFERENCE_FASTA = 
		new FileOption(
			"Reference Fasta", "rfasta", "Reference fasta file",
			false, true
		);
	public static final FileOption OPTION_REFERENCE_DB = 
		new FileOption(
			"Reference Database", "rdb", "Reference database directory",
			false, true
		);
	public static final FileOption OPTION_TARGET_FASTA = 
		new FileOption(
			"Target Fasta", "tfasta", "Target fasta file",
			false, true
		);
	public static final FileOption OPTION_TARGET_DB = 
		new FileOption(
			"Target Database", "tdb", "Target database directory",
			false, true
		);
	public static final StringOption OPTION_OUTPUT_NAME = 
		new StringOption(
			"Output name", "output", "Output name", 
			false, true
		);
	
	public SplignCompartCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "spligncompart";
	}
	
	@Override
	public String getDescriptiveName() {
		return "Splign-Compart (NCBI)";
	}

	@Override
	public String getDescription() {
		return "Applies the Splign-Compart-Bedtools pipeline";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final File referenceFastaFile = parameters.getSingleValue(OPTION_REFERENCE_FASTA);
		final File referenceDBDirectory = parameters.getSingleValue(OPTION_REFERENCE_DB);
		final File targetFastaFile = parameters.getSingleValue(OPTION_TARGET_FASTA);
		final File targetDBDirectory = parameters.getSingleValue(OPTION_TARGET_DB);
		final String outputName = parameters.getSingleValue(OPTION_OUTPUT_NAME);
		
		this.controller.splignCompart(
			new DefaultNucleotideFasta(referenceFastaFile),
			new DefaultNucleotideDatabase(referenceDBDirectory),
			new DefaultNucleotideFasta(targetFastaFile),
			new DefaultNucleotideDatabase(targetDBDirectory),
			outputName
		);
	}
}
