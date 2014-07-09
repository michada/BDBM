package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;

import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideFasta;

public class SplignCompartCommand extends BDBMCommand {
	public static final FileOption OPTION_GENOME_FASTA = 
		new FileOption(
			"Genome Fasta", "gfasta", "Genome fasta file",
			false, true
		);
	public static final FileOption OPTION_GENOME_DB = 
		new FileOption(
			"Genome Database", "gdb", "Genome database directory",
			false, true
		);
	public static final FileOption OPTION_CDS_FASTA = 
		new FileOption(
			"CDS Fasta", "cfasta", "CDS fasta file",
			false, true
		);
	public static final FileOption OPTION_CDS_DB = 
		new FileOption(
			"CDS Database", "cdb", "CDS database directory",
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
		final File genomeFastaFile = parameters.getSingleValue(OPTION_GENOME_FASTA);
		final File genomeDBDirectory = parameters.getSingleValue(OPTION_GENOME_DB);
		final File cdsFastaFile = parameters.getSingleValue(OPTION_CDS_FASTA);
		final File cdsDBDirectory = parameters.getSingleValue(OPTION_CDS_DB);
		final String outputName = parameters.getSingleValue(OPTION_OUTPUT_NAME);
		
		this.controller.splignCompart(
			new DefaultNucleotideFasta(genomeFastaFile),
			new DefaultNucleotideDatabase(genomeDBDirectory),
			new DefaultNucleotideFasta(cdsFastaFile),
			new DefaultNucleotideDatabase(cdsDBDirectory),
			outputName
		);
	}
}
