package es.uvigo.esei.sing.bdbm.fasta.naming;

import java.util.Collections;
import java.util.Set;


public class EmblNameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "emb";
	}

	@Override
	public String getDescription() {
		return "EMBL";
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
