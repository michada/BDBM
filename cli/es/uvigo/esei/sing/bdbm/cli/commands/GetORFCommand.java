package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.IntegerOption;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideFasta;

public class GetORFCommand extends BDBMCommand {
	public static final FileOption OPTION_FASTA = 
		new FileOption(
			"Fasta", "fasta", "Input fasta (nucleotide) file",
			false, true
		);
	public static final IntegerOption OPTION_MIN_SIZE =
		new IntegerOption(
			"Min. Size", "min", "Minimum ORF size", "300"
		);
	public static final IntegerOption OPTION_MAX_SIZE =
		new IntegerOption(
			"Max. Size", "max", "Maximum ORF size", "10000"
		);
	public static final StringOption OPTION_OUTPUT_NAME = 
		new StringOption(
			"Output name", "output", "Output name", 
			false, true
		);
	
	public GetORFCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "getorf";
	}
	
	@Override
	public String getDescriptiveName() {
		return "Get ORF (EMBOSS)";
	}

	@Override
	public String getDescription() {
		return "Performs a 'getorf' (EMBOSS)";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final File fastaFile = parameters.getSingleValue(OPTION_FASTA);
		final Integer minSize = parameters.getSingleValue(OPTION_MIN_SIZE);
		final Integer maxSize = parameters.getSingleValue(OPTION_MAX_SIZE);
		final String outputName = parameters.getSingleValue(OPTION_OUTPUT_NAME);
		
		this.controller.getORF(
			new DefaultNucleotideFasta(fastaFile),
			minSize,
			maxSize,
			outputName
		);
	}
}
