package es.uvigo.esei.sing.bdbm.fasta.naming;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class AbstractStandardNameSummarizer
implements StandardNameSummarizer {
	protected final static String PART_PATTERN = "\\|[\\p{Graph}]+";
	protected final static String OPTIONAL_PART_PATTERN = "\\|[\\p{Graph}]*";
	
	protected String separator = "_";
	
	protected abstract int getNumOfParts();
	protected abstract int[] getSelectedIndexes();
	
	protected int[][] getSelectedIndexesAlternatives() {
		return new int[][] { this.getSelectedIndexes() };
	}

	protected Set<Integer> getOptionalIndexes() {
		return Collections.emptySet();
	}
	
	@Override
	public String getSeparator() {
		return separator;
	}
	
	@Override
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	protected String getPattern() {
		final StringBuilder pattern = new StringBuilder("^>" + this.getPrefix());
		final Set<Integer> optionalIndexes = this.getOptionalIndexes();
		
		for (int i = 0; i < this.getNumOfParts(); i++) {
			if (optionalIndexes.contains(i)) {
				pattern.append(OPTIONAL_PART_PATTERN);
			} else {
				pattern.append(PART_PATTERN);
			}
		}
		pattern.append("(\\p{Blank}.*)?$");
		
		return pattern.toString();
	}
	
	@Override
	public boolean recognizes(String name) {
		return Pattern.matches(this.getPattern(), name);
	}
	
	@Override
	public String summarize(String name) {
		if (this.recognizes(name)) {
			name = name.replaceAll("\t", " ");
			if (name.contains(" ")) 
				name = name.substring(0, name.indexOf(' '));
			
			final String[] parts = name.split("[| \t]");
			
			for (int[] partsToReturn : getSelectedIndexesAlternatives()) {
				final StringBuilder sbName = new StringBuilder(">");
				
				for (int partIndex : partsToReturn) {
					final String part = parts[partIndex + 1].trim();
					
					if (!part.isEmpty()) {
						if (sbName.length() > 1) sbName.append(this.getSeparator());
						sbName.append(part);
					}
				}
				
				if (sbName.length() > 1) return sbName.toString();
			}
			
			throw new IllegalArgumentException("Invalid sequence name");
		} else {
			throw new IllegalArgumentException("Invalid sequence name");
		}
	}
}