package es.uvigo.esei.sing.bdbm.cli.commands.converters;

import es.uvigo.ei.sing.yaacli.AbstractOptionConverter;
import es.uvigo.ei.sing.yaacli.SingleParameterValue;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public class SequenceTypeOptionConverter extends AbstractOptionConverter<SequenceType> {
	@Override
	public SequenceType convert(SingleParameterValue value) {
		try {
			return SequenceType.valueOf(value.getValue());
		} catch (IllegalArgumentException e) {
			return SequenceType.forDBType(value.getValue());
		}
	}

	@Override
	public Class<SequenceType> getTargetClass() {
		return SequenceType.class;
	}

	@Override
	public boolean canConvert(SingleParameterValue value) {
		try {
			this.convert(value);
			return true;
		} catch (IllegalArgumentException iae) {
			return false;
		}
	}
}
