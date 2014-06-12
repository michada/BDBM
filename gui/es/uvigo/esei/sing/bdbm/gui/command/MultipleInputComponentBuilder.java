package es.uvigo.esei.sing.bdbm.gui.command;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.esei.sing.bdbm.gui.command.input.InputComponentBuilder;

public class MultipleInputComponentBuilder implements InputComponentBuilder {
	@Override
	public boolean canHandle(Option<?> option) {
		return option.isMultiple();
	}

	@Override
	public <T> Component createFor(
		Component parent, 
		Option<T> option,
		ParameterValuesReceiver receiver
	) {
		final JPanel inputComponent = new JPanel(new BorderLayout());
		
		
		
		return inputComponent;
	}
}
