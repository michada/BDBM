package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.BLASTPCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;

public class ExternalBLASTPCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;

	public ExternalBLASTPCommandDialog(
		BDBMController controller, 
		BLASTPCommand command
	) {
		this(controller, command, null);
	}
	
	public ExternalBLASTPCommandDialog(
		BDBMController controller, 
		BLASTPCommand command,
		Parameters defaultParameters
	) {
		super(controller, command, defaultParameters);
		
		this.setPreferredSize(new Dimension(400, 310));
	}

	@Override
	protected <T> Component createComponentForOption(
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		if (option.equals(BLASTPCommand.OPTION_DATABASE)) {
			final ProteinDatabase[] proteinDatabases = 
				this.controller.listProteinDatabases();
			final JComboBox<ProteinDatabase> cmbDatabases =
				new JComboBox<>(proteinDatabases);
			
			if (receiver.hasOption(option)) {
				for (ProteinDatabase database : proteinDatabases) {
					if (database.getName().equals(receiver.getValue(option))) {
						cmbDatabases.setSelectedItem(database);
						break;
					}
				}
			} else {
				final Object value = cmbDatabases.getSelectedItem();

				if (value != null) {
					final ProteinDatabase database = (ProteinDatabase) value;
					receiver.setValue(
						option, 
						new File(database.getDirectory(), database.getName()).getAbsolutePath()
					); 
				}
			}
			
			final ActionListener alDatabases = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final Object item = cmbDatabases.getSelectedItem();
					
					if (item == null) {
						receiver.setValue(option, (String) null);
					} else if (item instanceof ProteinDatabase) {
						final ProteinDatabase database = (ProteinDatabase) item;
						final File dbDirectory = database.getDirectory();
						receiver.setValue(
							option, new File(dbDirectory, database.getName()).getAbsolutePath()
						);
					}
				}
			};
			alDatabases.actionPerformed(null);
			
			cmbDatabases.addActionListener(alDatabases);
			
			return cmbDatabases;
		} else {
			return super.createComponentForOption(option, receiver);
		}
	}
}
