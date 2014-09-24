package es.uvigo.esei.sing.bdbm.fasta.naming;


public class GenInfoIntegratedDatabaseNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "gi";
	}
	
	@Override
	public String getDescription() {
		return "GenInfo Integrated Database";
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
