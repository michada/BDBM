package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideFasta;

public class SplignCompartCommand extends BDBMCommand {
	public static final FileOption OPTION_SOURCE_FASTA = 
		new FileOption(
			"Source Fasta", "sfasta", "Source fasta file",
			false, true
		);
	public static final FileOption OPTION_SOURCE_DB = 
		new FileOption(
			"Source Database", "sdb", "Source database directory",
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
		final File sourceFastaFile = parameters.getSingleValue(OPTION_SOURCE_FASTA);
		final File sourceDBDirectory = parameters.getSingleValue(OPTION_SOURCE_DB);
		final File targetFastaFile = parameters.getSingleValue(OPTION_TARGET_FASTA);
		final File targetDBDirectory = parameters.getSingleValue(OPTION_TARGET_DB);
		final String outputName = parameters.getSingleValue(OPTION_OUTPUT_NAME);
		
		this.controller.splignCompart(
			new DefaultNucleotideFasta(sourceFastaFile),
			new DefaultNucleotideDatabase(sourceDBDirectory),
			new DefaultNucleotideFasta(targetFastaFile),
			new DefaultNucleotideDatabase(targetDBDirectory),
			outputName
		);
	}
}
