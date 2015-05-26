package es.uvigo.esei.sing.bdbm.gui.command.input;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.OptionConverter;
import es.uvigo.ei.sing.yaacli.SingleParameterValue;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;

public class EnumInputComponentBuilder implements InputComponentBuilder {
	@Override
	public boolean canHandle(Option<?> option) {
		return !option.isMultiple() && option.getConverter().getTargetClass().isEnum();
	}

	@Override
	public <T> JComboBox<T> createFor(
		final Component parent, 
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		if (!this.canHandle(option))
			throw new IllegalArgumentException("Unsupported option type");
		
		final OptionConverter<T> converter = option.getConverter();
		final T[] enumConstants = converter.getTargetClass().getEnumConstants();
		
		final JComboBox<T> cmbEnum;
		if (option.isOptional()) {
			@SuppressWarnings("unchecked")
			final T[] enumConstantsWithEmpty = (T[]) new Object[enumConstants.length+1];
			System.arraycopy(enumConstants, 0, enumConstantsWithEmpty, 1, enumConstants.length);
			enumConstantsWithEmpty[0] = null;
			
			cmbEnum = new JComboBox<>(enumConstantsWithEmpty);
		} else {
			cmbEnum = new JComboBox<>(enumConstants);
		}
		
		
		if (receiver.hasValue(option)) {
			final SingleParameterValue value = new SingleParameterValue(receiver.getValue(option));
			
			cmbEnum.setSelectedItem(converter.convert(value));
		} else if (!option.isOptional()) {
			cmbEnum.setSelectedIndex(0);
			receiver.setValue(option, ((Enum<?>) enumConstants[0]).name());
		}
		
		cmbEnum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Object selectedItem = cmbEnum.getSelectedItem();
				
				if (selectedItem instanceof Enum) {
					receiver.setValue(option, ((Enum<?>) selectedItem).name());
				} else {
					receiver.setValue(option, (String) null);
				}
			}
		});
		
		return cmbEnum;
	}

}
