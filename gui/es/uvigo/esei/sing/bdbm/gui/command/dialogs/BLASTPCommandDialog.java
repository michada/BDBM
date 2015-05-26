package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.BLASTPCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry.ProteinQuery;

public class BLASTPCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;

	public BLASTPCommandDialog(
		BDBMController controller, 
		BLASTPCommand command
	) {
		this(controller, command, null);
	}
	
	public BLASTPCommandDialog(
		BDBMController controller, 
		BLASTPCommand command,
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
		} else if (option.equals(BLASTPCommand.OPTION_QUERY)) {
			final JPanel panel = new JPanel(new GridLayout(2, 1));
			
			final JComboBox<ProteinSearchEntry> cmbSearchEntries = new JComboBox<>(
				this.controller.listProteinSearchEntries()
			);
			final JComboBox<ProteinQuery> cmbQueries = new JComboBox<>();
			
			final ActionListener alQueries = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
//					final Object item = cmbQueries.getSelectedItem();
//					
//					if (item == null) {
//						receiver.setValue(option, (String) null);
//					} else if (item instanceof ProteinQuery) {
//						receiver.setValue(option, ((ProteinQuery) item).getBaseFile().getAbsolutePath());
//					}
					
					BLASTPCommandDialog.this.updateQuery((ProteinQuery) cmbQueries.getSelectedItem(), receiver);
				}
			};
			
			final ActionListener alSearchEntries = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final ProteinSearchEntry searchEntry = 
						(ProteinSearchEntry) cmbSearchEntries.getSelectedItem();
					
					if (searchEntry != null && !searchEntry.listQueries().isEmpty()) {
						cmbQueries.setModel(
							new DefaultComboBoxModel<>(
								new Vector<ProteinQuery>(searchEntry.listQueries())
							)
						);
					} else {
						cmbQueries.setModel(new DefaultComboBoxModel<ProteinQuery>());
					}
					
					alQueries.actionPerformed(null);
				}
			};
			
			alSearchEntries.actionPerformed(null);
			
			cmbSearchEntries.addActionListener(alSearchEntries);
			cmbQueries.addActionListener(alQueries);
			
			panel.add(cmbSearchEntries);
			panel.add(cmbQueries);
			
			return panel;
		} else {
			return super.createComponentForOption(option, receiver);
		}
	}
	
	protected void updateQuery(ProteinQuery query, ParameterValuesReceiver receiver) {
		if (query == null) {
			receiver.setValue(BLASTPCommand.OPTION_QUERY, (String) null);
		} else {
			receiver.setValue(BLASTPCommand.OPTION_QUERY, query.getBaseFile().getAbsolutePath());
		}
		
		this.updateButtonOk();
	}
}
