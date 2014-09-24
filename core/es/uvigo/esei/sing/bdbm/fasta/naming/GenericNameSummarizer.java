package es.uvigo.esei.sing.bdbm.fasta.naming;

import java.util.regex.Pattern;

public class GenericNameSummarizer
implements NameSummarizer {
	protected final static String NAME_PATTERN = "^>\\p{Graph}+(\\|\\p{Graph}*)+(\\p{Blank}.*)?$";

	private int[] selectedIndexes;
	
	public GenericNameSummarizer() {
		this(0);
	}
	
	public GenericNameSummarizer(int ... selectedIndexes) {
		this.selectedIndexes = selectedIndexes;
	}

	public int[] getSelectedIndexes() {
		return selectedIndexes;
	}
	
	public void setSelectedIndexes(int[] selectedIndexes) {
		this.selectedIndexes = selectedIndexes;
	}
	
	@Override
	public boolean recognizes(String name) {
		return Pattern.matches(NAME_PATTERN, name);
	}
	
	@Override
	public String summarize(String name) {
		if (this.recognizes(name)) {
			name = name.replaceAll("\t", " ");
			if (name.contains(" ")) 
				name = name.substring(0, name.indexOf(' '));
			
			final String[] parts = name.split("[| \t]");
			
			final StringBuilder sbName = new StringBuilder(">");
			for (int partIndex : getSelectedIndexes()) {
				final String part = parts[partIndex + 1].trim();
				
				if (!part.isEmpty()) {
					if (sbName.length() > 1) sbName.append('_');
					sbName.append(part);
				}
			}
			
			if (sbName.length() > 1) return sbName.toString();
			else throw new IllegalArgumentException("Invalid sequence name");
		} else {
			throw new IllegalArgumentException("Invalid sequence name");
		}
	}
}