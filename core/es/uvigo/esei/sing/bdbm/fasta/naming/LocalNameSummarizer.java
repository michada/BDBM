package es.uvigo.esei.sing.bdbm.fasta.naming;


public class LocalNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "lcl";
	}
	
	@Override
	public String getDescription() {
		return "Local";
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
