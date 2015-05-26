package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.TBLASTXCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;

public class ExternalTBLASTXCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;
	
	public ExternalTBLASTXCommandDialog(
		BDBMController controller, 
		TBLASTXCommand command
	) {
		this(controller, command, null);
	}

	public ExternalTBLASTXCommandDialog(
		BDBMController controller, 
		TBLASTXCommand command,
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
		if (option.equals(TBLASTXCommand.OPTION_DATABASE)) {
			final JComboBox<NucleotideDatabase> cmbDatabases = new JComboBox<>(
					this.controller.listNucleotideDatabases()
				);
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
