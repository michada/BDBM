package es.uvigo.esei.sing.bdbm.gui.command;

import java.util.List;

import es.uvigo.ei.sing.yaacli.Option;

public interface ParameterValuesReceiver {
	public abstract String getValue(Option<?> option);
	public List<String> getValues(Option<?> option);
	public abstract boolean hasOption(Option<?> option);
	public abstract boolean hasValue(Option<?> option);
	public abstract boolean removeValue(Option<?> option);
	public abstract void setValue(Option<?> option, String value);
	public abstract void setValue(Option<?> option, List<String> value);
}