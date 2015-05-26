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
import es.uvigo.esei.sing.bdbm.cli.commands.TBLASTXCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry.NucleotideQuery;

public class TBLASTXCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;
	
	public TBLASTXCommandDialog(
		BDBMController controller, 
		TBLASTXCommand command
	) {
		this(controller, command, null);
	}

	public TBLASTXCommandDialog(
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
		} else if (option.equals(TBLASTXCommand.OPTION_QUERY)) {
			final JPanel panel = new JPanel(new GridLayout(2, 1));
			
			final JComboBox<NucleotideSearchEntry> cmbSearchEntries = new JComboBox<>(
					this.controller.listNucleotideSearchEntries()
				);
			final JComboBox<NucleotideQuery> cmbQueries = new JComboBox<>();
			
			final ActionListener alQueries = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final Object item = cmbQueries.getSelectedItem();
					
					if (item == null) {
						receiver.setValue(option, (String) null);
					} else if (item instanceof NucleotideQuery) {
						receiver.setValue(option, ((NucleotideQuery) item).getBaseFile().getAbsolutePath());
					}
				}
			};
			
			final ActionListener alSearchEntries = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final NucleotideSearchEntry searchEntry = 
						(NucleotideSearchEntry) cmbSearchEntries.getSelectedItem();
					
					if (searchEntry != null && !searchEntry.listQueries().isEmpty()) {
						cmbQueries.setModel(
							new DefaultComboBoxModel<>(
								new Vector<NucleotideQuery>(searchEntry.listQueries())
							)
						);
					} else {
						cmbQueries.setModel(new DefaultComboBoxModel<NucleotideQuery>());
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
}
