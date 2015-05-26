package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.GetORFCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

public class GetORFCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;

	public GetORFCommandDialog(
		BDBMController controller, 
		GetORFCommand command
	) {
		this(controller, command, null);
	}
	
	public GetORFCommandDialog(
		BDBMController controller, 
		GetORFCommand command,
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
		if (option.equals(GetORFCommand.OPTION_FASTA)) {
			final NucleotideFasta[] nucleotideFasta = 
				this.controller.listNucleotideFastas();
			final JComboBox<NucleotideFasta> cmbFastas =
				new JComboBox<>(nucleotideFasta);
			
			if (this.hasDefaultOption(option)) {
				final String defaultFasta = this.getDefaultOptionString(option);
				for (NucleotideFasta fasta : nucleotideFasta) {
					if (fasta.getFile().getAbsolutePath().equals(defaultFasta)) {
						cmbFastas.setSelectedItem(fasta);
					}
				}
			}/* else {
				final Object value = cmbFastas.getSelectedItem();

				if (value != null) {
					final NucleotideFasta fasta = (NucleotideFasta) value;
					receiver.setValue(
						option, 
						fasta.getFile().getAbsolutePath()
					); 
				}
			}*/
			
			final ActionListener alFastas = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final Object item = cmbFastas.getSelectedItem();
					
					if (item == null) {
						receiver.setValue(option, (String) null);
					} else if (item instanceof NucleotideFasta) {
						final NucleotideFasta fasta = (NucleotideFasta) item;
						receiver.setValue(
							option, fasta.getFile().getAbsolutePath()
						);
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
