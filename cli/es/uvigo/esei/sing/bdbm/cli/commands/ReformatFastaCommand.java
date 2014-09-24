package es.uvigo.esei.sing.bdbm.cli.commands;

import java.io.File;
import java.util.List;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.StringConstructedOption;
import es.uvigo.ei.sing.yacli.StringOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.EnumOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.IntegerOption;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.SequenceTypeOptionConverter;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.fasta.FastaUtils;
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
			"Length of the sequence fragments (Negative values mean no changes, and zero value means no line break)", 
			-1
		);
	
	public static final EnumOption<FastaUtils.RenameMode> OPTION_RENAMING_MODE =
		new EnumOption<>(
			"Renaming Mode", 
			"mode", 
			"Renaming mode:\n"
			+ "\tNONE: No renaming\n" 
			+ "\tSMART: Recognices and summarizes the most common sequence name formats\n"
			+ "\tPREFIX: Replaces sequences names with a prefix followed by a sequence id\n"
			+ "\tGENERIC: If sequence name is in format <src>|<val0>|<val1>|<val2>..., then replaces name with the <valX> selected",
			FastaUtils.RenameMode.NONE,
			false, true, false
		);
	
	public static final StringConstructedOption<Integer> OPTION_INDEXES =
		new StringConstructedOption<Integer>(
			"Indexes",
			"indexes",
			"Indexes selected for the \"Generic\" renaming method",
			true, true, true
		) {};
	
	public static final StringOption OPTION_PREFIX =
		new StringOption(
			"Prefix", 
			"prefix", 
			"Prefix for the \"Prefix\" renaming method", 
			true, true
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
		final FastaUtils.RenameMode renameMode = parameters.getSingleValue(OPTION_RENAMING_MODE);
		final Integer fragmentLength = parameters.getSingleValue(OPTION_FRAGMENT_LENGTH);
		final List<Integer> indexes = parameters.getAllValues(OPTION_INDEXES);
		final String prefix = parameters.getSingleValue(OPTION_PREFIX);

		final Object additionalParameters;
		switch (renameMode) {
		case PREFIX:
			additionalParameters = prefix;
			break;
		case GENERIC:
			final int[] indexesArray = new int[indexes.size()];
			int i = 0;
			for (Integer index : indexes) {
				indexesArray[i++] = index;
			}
			
			additionalParameters = indexesArray;
			break;
		default:
			additionalParameters = null;
		}
		
		this.controller.reformatFasta(
			renameMode, 
			AbstractFasta.newFasta(fastaType, fastaFile), 
			fragmentLength == null ? -1 : fragmentLength, 
			additionalParameters
		);
	}
}
