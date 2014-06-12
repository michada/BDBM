package es.uvigo.esei.sing.bdbm.gui.command.input;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;

public class DefaultInputComponentBuilder implements InputComponentBuilder {
	private final static Logger LOG = LoggerFactory.getLogger(DefaultInputComponentBuilder.class);
	
	@Override
	public boolean canHandle(Option<?> option) {
		return !option.isMultiple();
	}
	
	@Override
	public <T> Component createFor(
		final Component parent, 
		final Option<T> option,
		final ParameterValuesReceiver receiver
	) {
		if (!this.canHandle(option))
			throw new IllegalArgumentException("Unsupported option type");
		
		if (option.requiresValue()) {
			final JTextField txt = new JTextField();
			
			if (receiver.hasOption(option)) {
				txt.setText(receiver.getValue(option));
			}
			
			final KeyAdapter txtListener = new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					try {
						if (txt.getText().isEmpty()) {
							receiver.removeValue(option);
						} else {
							receiver.setValue(option, txt.getText());
						}
					} catch (Exception ex) {
						LOG.error("Error setting option value", ex);
					}
				}
			};
			
			txtListener.keyReleased(null);
			txt.addKeyListener(txtListener);
			
			return txt;
		} else {
			final JCheckBox chk = new JCheckBox();

			final ActionListener chkListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (chk.isSelected()) {
						receiver.setValue(option, "true");
					} else {
						receiver.removeValue(option);
					}
				}
			};
			
			chkListener.actionPerformed(null);
			chk.addActionListener(chkListener);
			
			return chk;
		}
	}
}
