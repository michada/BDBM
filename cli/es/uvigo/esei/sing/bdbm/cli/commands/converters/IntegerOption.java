package es.uvigo.esei.sing.bdbm.cli.commands.converters;

import es.uvigo.ei.sing.yacli.DefaultValuedStringConstructedOption;

public class IntegerOption extends DefaultValuedStringConstructedOption<Integer> {
	public IntegerOption(
		String paramName, String shortName,
		String description, Integer defaultValue
	) {
		super(paramName, shortName, description, defaultValue.toString());
	}
	
	public IntegerOption(
		String paramName, String shortName,
		String description, String defaultValue
	) {
		this(paramName, shortName, description, Integer.valueOf(defaultValue));
	}
}
