package es.uvigo.esei.sing.bdbm.gui.command.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.esei.sing.bdbm.cli.commands.ConvertOrfToFastaCommand;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.ParameterValuesReceiver;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideORF;

public class ConvertOrfToFastaCommandDialog extends CommandDialog {
	private static final long serialVersionUID = 1L;
	
	private JComboBox<NucleotideORF> cmbORFs;
	private ActionListener alORFs;
	
	public ConvertOrfToFastaCommandDialog(
		BDBMController controller, 
		ConvertOrfToFastaCommand command
	) {
		this(controller, command, null);
	}
	
	public ConvertOrfToFastaCommandDialog(
		BDBMController controller, 
		ConvertOrfToFastaCommand command,
		Parameters defaultParameters
	) {
		super(controller, command, defaultParameters);
	}
	
	@Override
	protected <T> Component createComponentForOption(
		final Option<T> option,
		final ParameterValuesReceiver receiver
	) {
		if (this.cmbORFs == null) {
			final NucleotideORF[] orfs = controller.listNucleotideORFs();
			this.cmbORFs = new JComboBox<>(new DefaultComboBoxModel<NucleotideORF>(orfs));
		}
		
		if (option.equals(ConvertOrfToFastaCommand.OPTION_ORF_FILE)) {
			this.alORFs = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final NucleotideORF item = cmbORFs.getItemAt(cmbORFs.getSelectedIndex());
					
					if (item == null) {
						receiver.setValue(option, (String) null);
					} else {
						receiver.setValue(option, item.getFile().getAbsolutePath());
					}
				}
			};
			
			if (this.hasDefaultOption(option)) {
				final String orfPath = this.getDefaultOptionString(option);
				final int size = this.cmbORFs.getItemCount();
				
				for (int i = 0; i < size; i++) {
					final NucleotideORF orf = this.cmbORFs.getItemAt(i);
					
					if (orf.getFile().getAbsolutePath().equals(orfPath)) {
						this.cmbORFs.setSelectedIndex(i);
						break;
					}
				}
			}
			
			this.alORFs.actionPerformed(null);
			this.cmbORFs.addActionListener(alORFs);
			
			return this.cmbORFs;
		} else {
			return super.createComponentForOption(option, receiver);
		}
	}
}
