package es.uvigo.esei.sing.bdbm.gui.command.input;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;

public class BuildComponent {
	public static <T> Component forOption(
		final Component parent, 
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		return new DefaultInputComponentBuilder().createFor(parent, option, receiver);
	}
	
	public static <T> JComboBox<T> forEnum(
		final Component parent, 
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		return new EnumInputComponentBuilder().createFor(parent, option, receiver);
	}
	
	public static <T> Component forFile(
		final Component parent, 
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		return new FileInputComponentBuilder().createFor(parent, option, receiver);
	}
	
	public static <T> JCheckBox forBoolean(
		final Component parent, 
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		return new BooleanInputComponentBuilder().createFor(parent, option, receiver);
	}
}
