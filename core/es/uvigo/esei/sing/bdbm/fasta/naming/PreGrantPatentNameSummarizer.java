package es.uvigo.esei.sing.bdbm.fasta.naming;



public class PreGrantPatentNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "pgp";
	}
	
	@Override
	public String getDescription() {
		return "Pre-Grant Patent";
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
