package es.uvigo.esei.sing.bdbm.cli.commands.converters;

import java.math.BigDecimal;

import es.uvigo.ei.sing.yaacli.DefaultValuedStringConstructedOption;

public class BigDecimalOption extends DefaultValuedStringConstructedOption<BigDecimal> {
	public BigDecimalOption(
		String paramName, String shortName,
		String description, BigDecimal defaultValue
	) {
		super(paramName, shortName, description, defaultValue.toPlainString());
	}
	
	public BigDecimalOption(
		String paramName, String shortName,
		String description, String defaultValue
	) {
		this(paramName, shortName, description, new BigDecimal(defaultValue));
	}
}
