package es.uvigo.esei.sing.bdbm.fasta.naming;


public class GenInfoImportIdNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "gim";
	}
	
	@Override
	public String getDescription() {
		return "GenInfo Import ID";
	}

	@Override
	protected int getNumOfParts() {
		return 1;
	}

	@Override
	protected int[] getSelectedIndexes() {
		return new int[] { 0 };
	}
}
