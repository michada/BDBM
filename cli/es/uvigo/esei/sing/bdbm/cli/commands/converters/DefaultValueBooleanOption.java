package es.uvigo.esei.sing.bdbm.cli.commands.converters;

import es.uvigo.ei.sing.yacli.DefaultValuedOption;

public class DefaultValueBooleanOption extends DefaultValuedOption<Boolean> {
	public DefaultValueBooleanOption(
		String paramName, 
		String shortName,
		String description, 
		boolean value
	) {
		super(paramName, shortName, description, Boolean.toString(value), new BooleanOption.BooleanConverter());
	}
}
