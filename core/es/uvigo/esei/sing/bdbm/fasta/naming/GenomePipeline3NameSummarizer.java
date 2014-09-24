package es.uvigo.esei.sing.bdbm.fasta.naming;

import java.util.Collections;
import java.util.Set;


public class GenomePipeline3NameSummarizer 
extends AbstractStandardNameSummarizer {
	@Override
	public String getPrefix() {
		return "gpp";
	}
	
	@Override
	public String getDescription() {
		return "Genome Pipeline 3";
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
