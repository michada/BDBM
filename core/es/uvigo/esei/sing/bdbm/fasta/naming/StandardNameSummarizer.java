package es.uvigo.esei.sing.bdbm.fasta.naming;

public interface StandardNameSummarizer extends NameSummarizer {
	public abstract String getPrefix();
	public abstract String getDescription();
	
	public abstract String getSeparator();
	public abstract void setSeparator(String separator);
}
