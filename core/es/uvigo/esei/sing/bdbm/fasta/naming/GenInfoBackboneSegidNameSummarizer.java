package es.uvigo.esei.sing.bdbm.fasta.naming;


public class GenInfoBackboneSegidNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "bbs";
	}
	
	@Override
	public String getDescription() {
		return "GenInfo Backbone Segid";
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
