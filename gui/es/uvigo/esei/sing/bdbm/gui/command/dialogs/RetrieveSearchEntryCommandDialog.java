package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.ei.sing.yaacli.SingleParameterValue;
import es.uvigo.esei.sing.bdbm.cli.commands.RetrieveSearchEntryCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.gui.command.input.BuildComponent;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;

public class RetrieveSearchEntryCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;

	private final boolean accessionInfer;
	
	private JComboBox<Database> cmbDatabases;
	private ActionListener alDatabases;

	private JComboBox<String> cmbAccessions;
	private ActionListener alAccessions;
	
	public RetrieveSearchEntryCommandDialog(
		BDBMController controller, 
		RetrieveSearchEntryCommand command,
		boolean accessionInfer
	) {
		this(controller, command, null, accessionInfer);
	}
	
	public RetrieveSearchEntryCommandDialog(
		BDBMController controller, 
		RetrieveSearchEntryCommand command,
		Parameters defaultParameters,
		boolean accessionInfer
	) {
		super(controller, command, defaultParameters);
		
		this.accessionInfer = accessionInfer;
		
		this.pack();
	}

	private Database getSelectedDatabase() {
		return (Database) this.cmbDatabases.getSelectedItem();
	}
	
	@Override
	protected <T> Component createComponentForOption(
		final Option<T> option, 
		final ParameterValuesReceiver receiver
	) {
		if (this.cmbDatabases == null) {
			 this.cmbDatabases = new JComboBox<>();
		}
		
		if (option.equals(RetrieveSearchEntryCommand.OPTION_DB_TYPE)) {
			final ParameterValuesReceiver pvr = new ParameterValuesReceiverWrapper(receiver) {
				@Override
				public void setValue(Option<?> option, String value) {
					super.setValue(option, value);
					
					if (value == null) {
						cmbDatabases.setModel(new DefaultComboBoxModel<Database>());
					} else {
						final Object convertedValue = 
							option.getConverter().convert(new SingleParameterValue(value));
						
						final Database[] databases;
						if (convertedValue == SequenceType.NUCLEOTIDE) {
							databases = controller.listNucleotideDatabases();
						} else if (convertedValue == SequenceType.PROTEIN) {
							databases = controller.listProteinDatabases();
						} else {
							throw new IllegalArgumentException("Unknown option: " + convertedValue);
						}
						
						cmbDatabases.setModel(new DefaultComboBoxModel<>(databases));
						
						if (alDatabases != null)
							alDatabases.actionPerformed(null);
					}
				}
			};
			
			pvr.setValue(option, this.getDefaultOptionString(option));
			
			return BuildComponent.forEnum(this, option, pvr);
		} else if (option.equals(RetrieveSearchEntryCommand.OPTION_DATABASE)) {
			this.alDatabases = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final Database database = (Database) cmbDatabases.getSelectedItem();
					
					if (database == null) {
						receiver.setValue(option, (String) null);
					} else {
						receiver.setValue(option, database.getDirectory().getAbsolutePath());
						
						if (cmbAccessions != null && alAccessions != null) {
							cmbAccessions.setModel(new DefaultComboBoxModel<String>(new Vector<String>(
								database.listAccessions()
							)));
							
							alAccessions.actionPerformed(null);
						}
					}
				}
			};
			
			if (this.hasDefaultOption(option)) {
				final String dbPath = this.getDefaultOptionString(option);
				final int size = this.cmbDatabases.getItemCount();
				
				for (int i = 0; i < size; i++) {
					final Database database = (Database) this.cmbDatabases.getItemAt(i);
					
					if (database.getDirectory().getAbsolutePath().equals(dbPath)) {
						this.cmbDatabases.setSelectedIndex(i);
						break;
					}
				}
			}
			
			this.alDatabases.actionPerformed(null);
			this.cmbDatabases.addActionListener(alDatabases);
			
			return cmbDatabases;
		} else if (option.equals(RetrieveSearchEntryCommand.OPTION_ACCESSION)) {
			if (this.accessionInfer) {
				final Database database = this.getSelectedDatabase();
				if (database == null) {
					this.cmbAccessions = new JComboBox<>();
				} else {
					this.cmbAccessions = new JComboBox<>(new Vector<String>(
						database.listAccessions()
					));
				}
				
				this.alAccessions = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (cmbAccessions.getSelectedItem() != null)
							receiver.setValue(option, (String) cmbAccessions.getSelectedItem());
					}
				};
				
				this.alAccessions.actionPerformed(null);
				this.cmbAccessions.addActionListener(this.alAccessions);
				
				return cmbAccessions;
			} else {
				return super.createComponentForOption(option, receiver);
			}
		} else {
			return super.createComponentForOption(option, receiver);
		}
	}
}
