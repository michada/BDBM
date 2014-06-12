package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractORF;

public class ConvertOrfToFastaCommand extends BDBMCommand {
	public static final FileOption OPTION_ORF_FILE = 
		new FileOption(
			"ORF file", "orf", "Input ORF file", 
			false, true
		);
	public static final StringOption OPTION_OUTPUT_FASTA_NAME = 
		new StringOption(
			"Fasta name", "fasta", "Output fasta name", 
			false, true
		);
	
	public ConvertOrfToFastaCommand(BDBMController controller) {
		super(controller);
	}
	
	@Override
	public String getName() {
		return "orf-to-fasta";
	}

	@Override
	public String getDescriptiveName() {
		return "Convert ORF to Fasta";
	}

	@Override
	public String getDescription() {
		return "Converts an ORF into a Fasta file that can be used in BLAST operations";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final File input = parameters.getSingleValue(OPTION_ORF_FILE);
		final String outputDBName = parameters.getSingleValue(OPTION_OUTPUT_FASTA_NAME);
		
		this.controller.convertOrfToFasta(AbstractORF.newORF(input), outputDBName);
	}
}
