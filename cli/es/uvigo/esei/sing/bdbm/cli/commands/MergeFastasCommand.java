package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;
import java.util.List;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.SequenceTypeOptionConverter;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;

public class MergeFastasCommand extends BDBMCommand {
	public static final Option<SequenceType> OPTION_FASTA_TYPE = 
		new Option<SequenceType>(
			"Fasta Type", "fastatype", "Fasta type: prot (proteins) or nucl (nucleotides)", 
			false, true, 
			new SequenceTypeOptionConverter()
		);
	public static final FileOption OPTION_FASTAS = 
		new FileOption(
			"Fastas", "fastas", "Fasta files to be merged",
			false, true, true
		);
	public static final StringOption OPTION_OUTPUT_FASTA = 
		new StringOption(
			"Output Fasta", "out", "Resulting fasta file",
			false, true
		);
	
	public MergeFastasCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "merge_fastas";
	}

	@Override
	public String getDescriptiveName() {
		return "Merge Fastas";
	}

	@Override
	public String getDescription() {
		return "Merge two or more fasta files";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		final SequenceType fastaType = parameters.getSingleValue(OPTION_FASTA_TYPE);
		final List<File> fastaFiles = parameters.getAllValues(OPTION_FASTAS);
		final String outputFasta = parameters.getSingleValue(OPTION_OUTPUT_FASTA);
		
		final Fasta[] fastas = new Fasta[fastaFiles.size()];
		int i = 0;
		for (File fastaFile : fastaFiles) {
			fastas[i++] = AbstractFasta.newFasta(fastaType, fastaFile);
		}
		
		this.controller.mergeFastas(fastas, outputFasta);
	}
}
