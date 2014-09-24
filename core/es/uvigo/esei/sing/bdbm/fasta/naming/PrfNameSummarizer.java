package es.uvigo.esei.sing.bdbm.fasta.naming;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class PrfNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "prf";
	}
	
	@Override
	public String getDescription() {
		return "PRF";
	}
	
	@Override
	protected Set<Integer> getOptionalIndexes() {
		return new HashSet<Integer>(Arrays.asList(0, 1));
	}
	
	@Override
	protected int getNumOfParts() {
		return 2;
	}
	
	@Override
	protected int[] getSelectedIndexes() {
		return new int[0]; // Unused
	}
	
	@Override
	protected int[][] getSelectedIndexesAlternatives() {
		return new int[][] {
			{ 0 }, { 1 }
		};
	}
}
