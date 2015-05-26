package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.ei.sing.yaacli.SingleParameterValue;
import es.uvigo.esei.sing.bdbm.cli.commands.MakeBLASTDBCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.gui.command.input.BuildComponent;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;

public class MakeBLASTDBCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;

	private JComboBox<Fasta> cmbFastas;
	private ActionListener alFastas;
	
	public MakeBLASTDBCommandDialog(
		BDBMController controller, 
		MakeBLASTDBCommand command
	) {
		this(controller, command, null);
	}
	
	public MakeBLASTDBCommandDialog(
		BDBMController controller, 
		MakeBLASTDBCommand command,
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
		if (this.cmbFastas == null) {
			this.cmbFastas = new JComboBox<Fasta>();
		}
		
		if (option.equals(MakeBLASTDBCommand.OPTION_DB_TYPE)) {
			final ParameterValuesReceiver pvr = new ParameterValuesReceiverWrapper(receiver) {
				@Override
				public void setValue(Option<?> option, String value) {
					super.setValue(option, value);
					
					if (value == null) {
						cmbFastas.setModel(new DefaultComboBoxModel<Fasta>());
					} else {
						final Object convertedValue = 
							option.getConverter().convert(new SingleParameterValue(value));
						
						final Fasta[] fastas;
						if (convertedValue == SequenceType.NUCLEOTIDE) {
							fastas = controller.listNucleotideFastas();
						} else if (convertedValue == SequenceType.PROTEIN) {
							fastas = controller.listProteinFastas();
						} else {
							throw new IllegalArgumentException("Unknown option: " + convertedValue);
						}
						
						cmbFastas.setModel(new DefaultComboBoxModel<>(fastas));
						
						if (alFastas != null)
							alFastas.actionPerformed(null);
					}
				}
			};
			
			pvr.setValue(option, this.getDefaultOptionString(option));
			
			return BuildComponent.forEnum(this, option, pvr);
		} else if (option.equals(MakeBLASTDBCommand.OPTION_INPUT)) {
			this.alFastas = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final Object item = cmbFastas.getSelectedItem();
					
					if (item == null) {
						receiver.setValue(option, (String) null);
					} else if (item instanceof Fasta) {
						receiver.setValue(option, ((Fasta) item).getFile().getAbsolutePath());
					}
				}
			};
			
			if (this.hasDefaultOption(option)) {
				final String fastaPath = this.getDefaultOptionString(option);
				final int size = this.cmbFastas.getItemCount();
				
				for (int i = 0; i < size; i++) {
					final Fasta fasta = (Fasta) this.cmbFastas.getItemAt(i);
					
					if (fasta.getFile().getAbsolutePath().equals(fastaPath)) {
						this.cmbFastas.setSelectedIndex(i);
						break;
					}
				}
			}
			
			this.alFastas.actionPerformed(null);
			this.cmbFastas.addActionListener(alFastas);
			
			return cmbFastas;
		} else {
			return super.createComponentForOption(option, receiver);
		}
	}
}
