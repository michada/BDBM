package es.uvigo.esei.sing.bdbm.fasta.naming;



public class GeneralDatabaseReferenceNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "gnl";
	}
	
	@Override
	public String getDescription() {
		return "General Database Reference";
	}

	@Override
	protected int getNumOfParts() {
		return 2;
	}

	@Override
	protected int[] getSelectedIndexes() {
		return new int[] { 0, 1 };
	}
}
