package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.SplignCompartCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
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
		
		this.pack();
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
		} else {
			return super.createComponentForOption(option, receiver);
		}
	}
}
