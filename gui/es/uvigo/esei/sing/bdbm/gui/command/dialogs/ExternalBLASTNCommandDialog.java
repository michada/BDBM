package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.BLASTNCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;

public class ExternalBLASTNCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;

	public ExternalBLASTNCommandDialog(
		BDBMController controller, 
		BLASTNCommand command
	) {
		this(controller, command, null);
	}
	
	public ExternalBLASTNCommandDialog(
		BDBMController controller, 
		BLASTNCommand command,
		Parameters defaultParameters
	) {
		super(controller, command, defaultParameters);
		
		this.pack();
	}

	@Override
	protected <T> Component createComponentForOption(
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		if (option.equals(BLASTNCommand.OPTION_DATABASE)) {
			final NucleotideDatabase[] nucleotideDatabases = 
				this.controller.listNucleotideDatabases();
			final JComboBox<NucleotideDatabase> cmbDatabases =
				new JComboBox<>(nucleotideDatabases);
			
			if (receiver.hasOption(option)) {
				for (NucleotideDatabase database : nucleotideDatabases) {
					if (database.getName().equals(receiver.getValue(option))) {
						cmbDatabases.setSelectedItem(database);
					}
				}
			} else {
				final Object value = cmbDatabases.getSelectedItem();

				if (value != null) {
					final NucleotideDatabase database = (NucleotideDatabase) value;
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
					} else if (item instanceof NucleotideDatabase) {
						final NucleotideDatabase database = (NucleotideDatabase) item;
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
