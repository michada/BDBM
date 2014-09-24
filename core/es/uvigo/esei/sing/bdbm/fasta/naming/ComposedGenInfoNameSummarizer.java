package es.uvigo.esei.sing.bdbm.fasta.naming;


public class ComposedGenInfoNameSummarizer
implements NameSummarizer {
	protected final static String NAME_PATTERN = "^gi|\\p{Alnum}+|(\\|\\p{Alnum}*)+(\\p{Space}.*)?$";
	
	private int[] selectedIndexes = new int[] { 0 };
	
	public int[] getSelectedIndexes() {
		return selectedIndexes;
	}
	
	public void setSelectedIndexes(int[] selectedIndexes) {
		this.selectedIndexes = selectedIndexes;
	}
	
	@Override
	public boolean recognizes(String name) {
		if (name.startsWith("gi|") && name.length() > 3) {
			final int barIndex = name.indexOf('|', 3);
			if (barIndex != -1) {
				final String subname = name.substring(barIndex + 1);
				
				return NameSummarizerFactory.createNameSummarizer(subname) != null;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public String summarize(String name) {
		if (this.recognizes(name)) {
			final int barIndex = name.indexOf('|', 3);
			final String subname = name.substring(barIndex + 1);
			final NameSummarizer summarizer = NameSummarizerFactory.createNameSummarizer(subname);
			
			if (barIndex > 3)
				return name.substring(3, barIndex) + "_" + summarizer.summarize(subname);
			else
				return summarizer.summarize(subname);
		} else {
			throw new IllegalArgumentException("Invalid sequence name");
		}
	}
}