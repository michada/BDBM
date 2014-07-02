package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.IntegerOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.SequenceTypeOptionConverter;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractFasta;

public class ReformatFastaCommand extends BDBMCommand {
	public static final Option<SequenceType> OPTION_FASTA_TYPE = 
		new Option<SequenceType>(
			"Fasta Type", "fastatype", "Fasta type: prot (proteins) or nucl (nucleotides)", 
			false, true, 
			new SequenceTypeOptionConverter()
		);
	public static final FileOption OPTION_FASTA = 
		new FileOption(
			"Fasta", "fasta", "Source fasta file",
			false, true
		);
	public static final IntegerOption OPTION_FRAGMENT_LENGTH = 
		new IntegerOption(
			"Sequence Fragment Length", "length", 
			"Length of the sequence fragments (Zero or negative values mean no line break)", 
			0
		);
	
	public ReformatFastaCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "reformat_fasta";
	}
	
	@Override
	public String getDescriptiveName() {
		return "Reformat Fasta";
	}

	@Override
	public String getDescription() {
		return "Reformats a Fasta file to change the sequence fragments length. "
			+ "Zero or negative length means no fragmented sequences";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final SequenceType fastaType = parameters.getSingleValue(OPTION_FASTA_TYPE);
		final File fastaFile = parameters.getSingleValue(OPTION_FASTA);
		final Integer fragmentLength = parameters.getSingleValue(OPTION_FRAGMENT_LENGTH);
		
		this.controller.reformatFasta(
			AbstractFasta.newFasta(fastaType, fastaFile), 
			fragmentLength
		);
	}
}
