package es.uvigo.esei.sing.bdbm.fasta.naming;

import java.util.Collections;
import java.util.Set;


public class GenBankNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "gb";
	}
	
	@Override
	public String getDescription() {
		return "GenBank";
	}
	
	@Override
	protected Set<Integer> getOptionalIndexes() {
		return Collections.singleton(1);
	}

	@Override
	protected int getNumOfParts() {
		return 2;
	}

	@Override
	protected int[] getSelectedIndexes() {
		return new int[] { 0 };
	}
}
