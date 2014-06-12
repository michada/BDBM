package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.util.List;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;

abstract class AbstractParameterValuesReceiver implements
		ParameterValuesReceiver {
	@Override
	public String getValue(Option<?> option) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getValues(Option<?> option) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasOption(Option<?> option) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasValue(Option<?> option) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeValue(Option<?> option) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Option<?> option, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Option<?> option, List<String> value) {
		throw new UnsupportedOperationException();
	}
}
