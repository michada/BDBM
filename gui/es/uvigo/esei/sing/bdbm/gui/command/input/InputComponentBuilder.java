package es.uvigo.esei.sing.bdbm.gui.command.input;

import java.awt.Component;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;

public interface InputComponentBuilder {
	public boolean canHandle(Option<?> option);
	public <T> Component createFor(
		Component parent, Option<T> option, ParameterValuesReceiver receiver
	);
}
