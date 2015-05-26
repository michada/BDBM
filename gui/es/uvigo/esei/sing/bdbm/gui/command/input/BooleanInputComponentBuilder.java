package es.uvigo.esei.sing.bdbm.gui.command.input;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;

public class BooleanInputComponentBuilder implements InputComponentBuilder {
	@Override
	public boolean canHandle(Option<?> option) {
		return Boolean.class.equals(option.getConverter().getTargetClass());
	}
	
	@Override
	public <T> JCheckBox createFor(
		final Component parent, 
		final Option<T> option,
		final ParameterValuesReceiver receiver
	) {
		if (!this.canHandle(option))
			throw new IllegalArgumentException("Unsupported option type");
		
		final JCheckBox chk = new JCheckBox();
		
		if (receiver.hasValue(option)) {	
			chk.setSelected(Boolean.valueOf(receiver.getValue(option)));
		} else {
			receiver.setValue(option, Boolean.toString(chk.isSelected()));
		}

		chk.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				receiver.setValue(option, Boolean.toString(chk.isSelected()));
			}
		});
		
		return chk;
	}
}
