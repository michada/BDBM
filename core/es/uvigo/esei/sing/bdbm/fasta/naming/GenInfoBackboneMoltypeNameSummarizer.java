package es.uvigo.esei.sing.bdbm.fasta.naming;


public class GenInfoBackboneMoltypeNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "bbm";
	}
	
	@Override
	public String getDescription() {
		return "GenInfo Backbone Moltype";
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
