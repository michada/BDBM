package es.uvigo.esei.sing.bdbm.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class DatabaseFileFilter implements FileFilter {
	private final FilenameFilter suffixFilter;
	
	public DatabaseFileFilter(String dbSuffix) {
		final String suffix = dbSuffix.toLowerCase();
		
		this.suffixFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(suffix);
			}
		};
	}
	
	@Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory()) {
			return pathname.listFiles(this.suffixFilter).length > 0;
		} else {
			return false;
		}
	}
}
