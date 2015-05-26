package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.util.List;

import es.uvigo.ei.sing.yaacli.Option;
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
	public List<String> getValues(Option<?> option) {
		return super.getValues(option);
	}
	
	@Override
	public void setValue(Option<?> option, String value) {
		this.receiver.setValue(option, value);
	}
	
	@Override
	public void setValue(Option<?> option, List<String> value) {
		super.setValue(option, value);
	}
	
	@Override
	public boolean removeValue(Option<?> option) {
		return super.removeValue(option);
	}
	
	@Override
	public boolean hasOption(Option<?> option) {
		return super.hasOption(option);
	}
}
