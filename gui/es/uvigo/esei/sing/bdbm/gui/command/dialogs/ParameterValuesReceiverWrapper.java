package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;

public class ParameterValuesReceiverWrapper extends AbstractParameterValuesReceiver {
	private final ParameterValuesReceiver receiver;
	
	public ParameterValuesReceiverWrapper(ParameterValuesReceiver receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public boolean hasValue(Option<?> option) {
		return this.receiver.hasValue(option);
	}
	
	@Override
	public String getValue(Option<?> option) {
		return this.receiver.getValue(option);
	}
	
	@Override
	public void setValue(Option<?> option, String value) {
		this.receiver.setValue(option, value);
	}
}
