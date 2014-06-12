package es.uvigo.esei.sing.bdbm.cli.commands;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.SequenceTypeOptionConverter;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class ImportFastaCommand extends BDBMCommand {
	public static final Option<SequenceType> OPTION_IMPORT_TYPE = 
		new Option<SequenceType>(
			"Type", "type", "Fasta type: auto, nucl (nucleotides) or prot (proteins)", 
			false, true, 
			new SequenceTypeOptionConverter()
		);
	public static final FileOption OPTION_INPUT_FILE = 
		new FileOption(
			"File", "file", "Fasta file", 
			false, true
		);
	
	public ImportFastaCommand(BDBMController controller) {
		super(controller);
	}

	@Override
	public String getName() {
		return "import_fasta";
	}
	
	@Override
	public String getDescriptiveName() {
		return "Import Fasta";
	}

	@Override
	public String getDescription() {
		return "Imports a nucleotide or fasta file into the repository";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
		this.controller.importFasta(
			parameters.getSingleValue(OPTION_IMPORT_TYPE),
			parameters.getSingleValue(OPTION_INPUT_FILE)
		);
	}
}
