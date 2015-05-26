package es.uvigo.esei.sing.bdbm.cli.commands.converters;

import es.uvigo.ei.sing.yaacli.AbstractOptionConverter;
import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.SingleParameterValue;

public class BooleanOption extends Option<Boolean> {
	public BooleanOption(
		String paramName,
		String shortName,
		String description,
		boolean optional,
		boolean requiresValue,
		boolean isMultiple
	) {
		super(paramName, shortName, description, optional, requiresValue, isMultiple, new BooleanConverter());
	}

	public BooleanOption(
		String paramName,
		String shortName,
		String description,
		boolean optional,
		boolean requiresValue
	) {
		super(paramName, shortName, description, optional, requiresValue, new BooleanConverter());
	}
	
	protected static class BooleanConverter extends AbstractOptionConverter<Boolean> {
		@Override
		public Class<Boolean> getTargetClass() {
			return Boolean.class;
		}

		@Override
		public boolean canConvert(SingleParameterValue value) {
			return true;
		}

		@Override
		public Boolean convert(SingleParameterValue spv) {
			final String value = spv.getValue();
			
			if (value.equalsIgnoreCase("yes"))
				return Boolean.TRUE;
			else if (value.equalsIgnoreCase("no"))
				return Boolean.FALSE;
			else
				return Boolean.valueOf(value);
		}
	}
}
