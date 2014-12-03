package es.uvigo.esei.sing.bdbm.gui.configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.esei.sing.bdbm.gui.BDBMGUIController;
import es.uvigo.esei.sing.bdbm.gui.configuration.ConfigurationPanel.ConfigurationChangeEvent;
import es.uvigo.esei.sing.bdbm.gui.configuration.ConfigurationPanel.ConfigurationChangeEventListener;

public class ConfigurationDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationPanel.class);

	public ConfigurationDialog(BDBMGUIController controller) {
		super((Frame) null, "BDBM Configuration", true);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Frame owner) {
		super(owner);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Dialog owner) {
		super(owner);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Window owner) {
		super(owner);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Frame owner, boolean modal) {
		super(owner, modal);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Frame owner, String title) {
		super(owner, title);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Dialog owner, boolean modal) {
		super(owner, modal);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Dialog owner, String title) {
		super(owner, title);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Window owner, String title) {
		super(owner, title);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Window owner, String title,
			ModalityType modalityType) {
		super(owner, title, modalityType);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		this.init(controller);
	}

	public ConfigurationDialog(BDBMGUIController controller, Window owner, String title,
			ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		this.init(controller);
	}
	
	protected void init(final BDBMGUIController controller) {
		final JPanel panelMain = new JPanel(new BorderLayout());
		
		final JPanel panelButtons = new JPanel();
		panelButtons.setOpaque(true);
		panelButtons.setBackground(Color.WHITE);
		panelButtons.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		
		final ConfigurationPanel configurationPanel = new ConfigurationPanel(controller);
		
		final JButton btnStore = new JButton(new AbstractAction("Save changes") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				final PathsConfiguration configuration = configurationPanel.getConfiguration();
				
				try {
					if (controller.changePaths(configuration)) {
						JOptionPane.showMessageDialog(
							ConfigurationDialog.this,
							"Configuration succesfuly changed.",
							"Configuration Changed",
							JOptionPane.INFORMATION_MESSAGE
						);
					} else {
						JOptionPane.showMessageDialog(
							ConfigurationDialog.this,
							"No changes done.",
							"Configuration Change",
							JOptionPane.INFORMATION_MESSAGE
						);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(
						ConfigurationDialog.this,
						"Error changing configuration. Application might not work properly. Please, close application and check configuration file.",
						"Configuration Error",
						JOptionPane.ERROR_MESSAGE
					);
					
					ConfigurationDialog.LOG.error("Error changing configuration", e1);
				}
				
				ConfigurationDialog.this.setVisible(false);
			}
		});
		final JButton btnCancel = new JButton(new AbstractAction("Cancel") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationDialog.this.setVisible(false);
			}
		});
		
		configurationPanel.addConfigurationChangeListener(new ConfigurationChangeEventListener() {
			@Override
			public void configurationChanged(ConfigurationChangeEvent event) {
				btnStore.setEnabled(event.getConfiguration() != null);
			}
		});
		
		panelButtons.add(btnStore);
		panelButtons.add(btnCancel);
		
		panelMain.add(configurationPanel, BorderLayout.CENTER);
		panelMain.add(panelButtons, BorderLayout.SOUTH);
		
		this.setContentPane(panelMain);

		this.pack();
		this.setLocationRelativeTo(this.getOwner());
	}
}
