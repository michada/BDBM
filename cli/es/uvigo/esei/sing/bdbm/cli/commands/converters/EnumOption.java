package es.uvigo.esei.sing.bdbm.cli.commands.converters;

import es.uvigo.ei.sing.yaacli.AbstractOptionConverter;
import es.uvigo.ei.sing.yaacli.DefaultValuedOption;
import es.uvigo.ei.sing.yaacli.SingleParameterValue;

public class EnumOption<T extends Enum<T>> extends DefaultValuedOption<T> {
	public EnumOption(
		String paramName, String shortName,
		String description, T defaultValue
	) {
		super(paramName, shortName, description, defaultValue.name(), 
			new EnumConverter<T>(defaultValue.getDeclaringClass())
		);
	}
	
	public EnumOption(
		String paramName, String shortName,
		String description, Class<T> enumType, String defaultValue
	) {
		super(paramName, shortName, description, defaultValue, new EnumConverter<T>(enumType));
	}
	
	public EnumOption(
		String paramName, String shortName, 
		String description, T defaultValue,
		boolean optional, boolean requiresValue, boolean isMultiple
	) {
		super(
			paramName, shortName, 
			description, defaultValue.name(), 
			optional, requiresValue, isMultiple, 
			new EnumConverter<T>(defaultValue.getDeclaringClass())
		);
	}
	
	public EnumOption(
		String paramName, String shortName, 
		String description, Class<T> enumType, String defaultValue, 
		boolean optional, boolean requiresValue, boolean isMultiple
	) {
		super(
			paramName, shortName, 
			description, defaultValue, 
			optional, requiresValue, isMultiple, 
			new EnumConverter<T>(enumType)
		);
	}




	private final static class EnumConverter<E extends Enum<E>> extends AbstractOptionConverter<E> {
		private final Class<E> enumType;
		
		public EnumConverter(Class<E> enumType) {
			this.enumType = enumType;
		}

		@Override
		public Class<E> getTargetClass() {
			return this.enumType;
		}

		@Override
		public boolean canConvert(SingleParameterValue value) {
			try {
				Enum.valueOf(enumType, value.getValue());
				return true;
			} catch (IllegalArgumentException | NullPointerException e) {
				return false;
			}
		}

		@Override
		public E convert(SingleParameterValue value) {
			return Enum.valueOf(enumType, value.getValue());
		}
	}
}
