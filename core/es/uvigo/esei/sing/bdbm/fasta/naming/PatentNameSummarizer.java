package es.uvigo.esei.sing.bdbm.fasta.naming;



public class PatentNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "pat";
	}
	
	@Override
	public String getDescription() {
		return "Patent";
	}

	@Override
	protected int getNumOfParts() {
		return 3;
	}

	@Override
	protected int[] getSelectedIndexes() {
		return new int[] { 1 };
	}
}
