package es.uvigo.esei.sing.bdbm.controller;

import java.io.File;
import java.io.FilenameFilter;

class SuffixFilenameFilter implements FilenameFilter {
	private final String suffix;
	
	private SuffixFilenameFilter(String suffix) {
		this.suffix = suffix.toLowerCase();
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(this.suffix);
	}
}
