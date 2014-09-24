package es.uvigo.esei.sing.bdbm.fasta.naming;

public interface NameSummarizer {
	public boolean recognizes(String name);
	public String summarize(String name);
}
