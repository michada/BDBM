package es.uvigo.esei.sing.bdbm.cli.commands.converters;

import java.io.File;

import es.uvigo.ei.sing.yaacli.StringConstructedOption;


public class FileOption extends StringConstructedOption<File> {
	public FileOption(String paramName, String shortName,
			String description, boolean optional, boolean requiresValue,
			boolean isMultiple) {
		super(paramName, shortName, description, optional, requiresValue, isMultiple);
	}

	public FileOption(String paramName, String shortName,
			String description, boolean optional, boolean requiresValue) {
		super(paramName, shortName, description, optional, requiresValue);
	}
}
