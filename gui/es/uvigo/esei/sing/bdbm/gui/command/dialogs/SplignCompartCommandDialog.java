package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.SplignCompartCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

public class SplignCompartCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;

	public SplignCompartCommandDialog(
		BDBMController controller, 
		SplignCompartCommand command
	) {
		this(controller, command, null);
	}
	
	public SplignCompartCommandDialog(
		BDBMController controller, 
		SplignCompartCommand command,
		Parameters defaultParameters
	) {
		super(controller, command, defaultParameters);
		
		this.setPreferredSize(new Dimension(400, 285));
	}
	
	@Override
	protected <T> Component createComponentForOption(
		final Option<T> option,
		final ParameterValuesReceiver receiver
	) {
		if (option.equals(SplignCompartCommand.OPTION_GENOME_FASTA) ||
			option.equals(SplignCompartCommand.OPTION_CDS_FASTA)
		) {
			final NucleotideFasta[] fastas = this.controller.listNucleotideFastas();
			
			final JComboBox<NucleotideFasta> cmbFastas = new JComboBox<>(fastas);
			
			if (receiver.hasOption(option)) {
				final String defaultFastaName = receiver.getValue(option);
				
				for (NucleotideFasta fasta : fastas) {
					if (fasta.getName().equals(defaultFastaName)) {
						cmbFastas.setSelectedItem(fasta);
						break;
					}
				}
			} else {
				final Object value = cmbFastas.getSelectedItem();

				if (value != null) {
					final NucleotideFasta fasta = (NucleotideFasta) value;
					receiver.setValue(option, fasta.getFile().getAbsolutePath()); 
				}
			}
			
			final ActionListener alFastas = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final Object item = cmbFastas.getSelectedItem();
					
					if (item == null) {
						receiver.setValue(option, (String) null);
					} else if (item instanceof NucleotideFasta) {
						final NucleotideFasta fasta = (NucleotideFasta) item;
						receiver.setValue(option, fasta.getFile().getAbsolutePath());
					}
				}
			};
			alFastas.actionPerformed(null);
			
			cmbFastas.addActionListener(alFastas);
			
			return cmbFastas;
		} else if (option.equals(SplignCompartCommand.OPTION_GENOME_DB) ||
			option.equals(SplignCompartCommand.OPTION_CDS_DB)
		) {
			final NucleotideDatabase[] nucleotideDatabases = 
					this.controller.listNucleotideDatabases();
				final JComboBox<NucleotideDatabase> cmbDatabases =
					new JComboBox<>(nucleotideDatabases);
				
				if (receiver.hasOption(option)) {
					for (NucleotideDatabase database : nucleotideDatabases) {
						if (database.getName().equals(receiver.getValue(option))) {
							cmbDatabases.setSelectedItem(database);
							break;
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
