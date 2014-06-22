package es.uvigo.esei.sing.bdbm.gui.configuration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EventListener;
import java.util.EventObject;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.uvigo.esei.sing.bdbm.gui.BDBMGUIController;

public class ConfigurationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final BDBMGUIController controller;
	
	private final JTextField txtRepository;
	private final JTextField txtBLAST;
	private final JTextField txtEMBOSS;
	private final JTextField txtNCBI;
	private final JButton btnBuildRepository;

	public ConfigurationPanel(BDBMGUIController controller) {
		super();
		
		this.controller = controller;
		
		this.setPreferredSize(new Dimension(600, 140));
		
		final GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		this.setLayout(layout);
		
		final JLabel lblRepository = new JLabel("Repository Path");
		final JLabel lblBLAST = new JLabel("BLAST Path");
		final JLabel lblEMBOSS = new JLabel("EMBOSS Path");
		final JLabel lblNCBI = new JLabel("NCBI Path");
		
		final File repositoryPath = this.controller.getEnvironment()
			.getRepositoryPaths().getBaseDirectory();
		final File blastBD = this.controller.getEnvironment()
			.getBLASTBinaries().getBaseDirectory();
		final File embossBD = this.controller.getEnvironment()
			.getEMBOSSBinaries().getBaseDirectory();
		final File ncbiBD = this.controller.getEnvironment()
			.getNCBIBinaries().getBaseDirectory();
		
		this.txtRepository = new JTextField(repositoryPath.getAbsolutePath());
		this.txtBLAST = new JTextField(blastBD == null ? "" : blastBD.getAbsolutePath());
		this.txtEMBOSS = new JTextField(embossBD == null ? "" : embossBD.getAbsolutePath());
		this.txtNCBI = new JTextField(ncbiBD == null ? "" : ncbiBD.getAbsolutePath());
		
		this.txtRepository.setEditable(false);
		this.txtBLAST.setEditable(false);
		this.txtEMBOSS.setEditable(false);
		this.txtNCBI.setEditable(false);
		
		final JButton btnRepository = new JButton("Select...");
		final JButton btnBLASTSelect = new JButton("Select...");
		final JButton btnEMBOSSSelect = new JButton("Select...");
		final JButton btnNCBISelect = new JButton("Select...");
		
		final JButton btnBLASTInPath = new JButton("In system path");
		final JButton btnEMBOSSInPath = new JButton("In system path");
		final JButton btnNCBIInPath = new JButton("In system path");
		
		this.btnBuildRepository = new JButton(new AbstractAction("Build") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationPanel.this.buildRepository();
			}
		});
		this.btnBuildRepository.setEnabled(false);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup()
				.addComponent(lblRepository, Alignment.CENTER)
				.addComponent(this.txtRepository)
				.addComponent(btnRepository)
				.addComponent(this.btnBuildRepository)
			)
			.addGroup(layout.createParallelGroup()
				.addComponent(lblBLAST, Alignment.CENTER)
				.addComponent(this.txtBLAST)
				.addComponent(btnBLASTSelect)
				.addComponent(btnBLASTInPath)
			)
			.addGroup(layout.createParallelGroup()
				.addComponent(lblEMBOSS, Alignment.CENTER)
				.addComponent(this.txtEMBOSS)
				.addComponent(btnEMBOSSSelect)
				.addComponent(btnEMBOSSInPath)
			)
			.addGroup(layout.createParallelGroup()
				.addComponent(lblNCBI, Alignment.CENTER)
				.addComponent(this.txtNCBI)
				.addComponent(btnNCBISelect)
				.addComponent(btnNCBIInPath)
			)
		);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup()
				.addComponent(lblRepository)
				.addComponent(lblBLAST)
				.addComponent(lblEMBOSS)
				.addComponent(lblNCBI)
			)
			.addGroup(layout.createParallelGroup()
				.addComponent(this.txtRepository)
				.addComponent(this.txtBLAST)
				.addComponent(this.txtEMBOSS)
				.addComponent(this.txtNCBI)
			)
			.addGroup(layout.createParallelGroup()
				.addComponent(btnRepository)
				.addComponent(btnBLASTSelect)
				.addComponent(btnEMBOSSSelect)
				.addComponent(btnNCBISelect)
			)
			.addGroup(layout.createParallelGroup()
				.addComponent(this.btnBuildRepository)
				.addComponent(btnBLASTInPath)
				.addComponent(btnEMBOSSInPath)
				.addComponent(btnNCBIInPath)
			)
		);
		
		final Callable<Boolean> callbackRepositorySelection = new Callable<Boolean>() {
			@Override
			public Boolean call() {
				if (ConfigurationPanel.this.isValidRepositoryPath()) {
					btnBuildRepository.setEnabled(false);
				} else {
					btnBuildRepository.setEnabled(true);
					
					if (JOptionPane.showConfirmDialog(
						ConfigurationPanel.this, 
						"Repository path does not exist or its structure is incomplete. Do you wish to build repository structure?",
						"Invalid Repository",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
					) == JOptionPane.YES_OPTION) {
						ConfigurationPanel.this.buildRepository();
					}
				}
				
				return true;
			}
		};
		btnRepository.addActionListener(
			new PathSelectionActionListener(this.txtRepository, callbackRepositorySelection)
		);
		
		final Callable<Boolean> callbackCheckBLAST = new Callable<Boolean>() {
			@Override
			public Boolean call() {
				if (ConfigurationPanel.this.isValidBLASTPath()) {
					return true;
				} else {
					JOptionPane.showMessageDialog(
						ConfigurationPanel.this, 
						"Invalid BLAST binaries path. Please, change the selected path",
						"Invalid Path",
						JOptionPane.ERROR_MESSAGE
					);
					
					return false;
				}
			}
		};
		btnBLASTSelect.addActionListener(
			new PathSelectionActionListener(this.txtBLAST, callbackCheckBLAST)
		);
		btnBLASTInPath.addActionListener(
			new SystemPathSelectionActionListener(this.txtBLAST, callbackCheckBLAST)
		);
		
		final Callable<Boolean> callbackCheckEMBOSS = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				if (ConfigurationPanel.this.isValidEMBOSSPath()) {
					return true;
				} else {
					JOptionPane.showMessageDialog(
						ConfigurationPanel.this, 
						"Invalid EMBOSS binaries path. Please, change the selected path",
						"Invalid Path",
						JOptionPane.ERROR_MESSAGE
					);
					
					return false;
				}
			}
		};
		btnEMBOSSSelect.addActionListener(
			new PathSelectionActionListener(this.txtEMBOSS, callbackCheckEMBOSS)
		);
		btnEMBOSSInPath.addActionListener(
			new SystemPathSelectionActionListener(this.txtEMBOSS, callbackCheckEMBOSS)
		);
		
		final Callable<Boolean> callbackCheckNCBI = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				if (ConfigurationPanel.this.isValidNCBIPath()) {
					return true;
				} else {
					JOptionPane.showMessageDialog(
						ConfigurationPanel.this, 
						"Invalid NCBI binaries path. Please, change the selected path",
						"Invalid Path",
						JOptionPane.ERROR_MESSAGE
					);
					
					return false;
				}
			}
		};
		btnNCBISelect.addActionListener(
			new PathSelectionActionListener(this.txtNCBI, callbackCheckNCBI)
		);
		btnNCBIInPath.addActionListener(
			new SystemPathSelectionActionListener(this.txtNCBI, callbackCheckNCBI)
		);
	}

	public void addConfigurationChangeListener(ConfigurationChangeEventListener listener) {
		this.listenerList.add(ConfigurationChangeEventListener.class, listener);
	}
	
	public void removeConfigurationChangeListener(ConfigurationChangeEventListener listener) {
		this.listenerList.remove(ConfigurationChangeEventListener.class, listener);
	}
	
	protected void fireChangeEvent(ConfigurationChangeEvent event) {
		final ConfigurationChangeEventListener[] listeners = 
			this.listenerList.getListeners(ConfigurationChangeEventListener.class);
		
		for (ConfigurationChangeEventListener listener : listeners) {
			listener.configurationChanged(event);
		}
	}
	
	protected void fireChange() {
		this.fireChangeEvent(new ConfigurationChangeEvent(this));
	}

	protected File getRepositoryDirectory() {
		return new File(this.txtRepository.getText());
	}

	protected String getBLASTPath() {
		return this.txtBLAST.getText().isEmpty() ?
			null : new File(this.txtBLAST.getText()).getAbsolutePath();
	}
	
	protected String getEMBOSSPath() {
		return this.txtEMBOSS.getText().isEmpty() ?
			null : new File(this.txtEMBOSS.getText()).getAbsolutePath();
	}
	
	protected String getNCBIPath() {
		return this.txtNCBI.getText().isEmpty() ?
			null : new File(this.txtNCBI.getText()).getAbsolutePath();
	}
	
	public boolean isValidRepositoryPath() {
		return this.controller.getEnvironment()
			.getRepositoryPaths()
			.checkBaseDirectory(getRepositoryDirectory());
	}
	
	public boolean isValidBLASTPath() {
		return this.controller.getManager().checkBLASTPath(getBLASTPath());
	}
	
	protected boolean isValidEMBOSSPath() {
		return this.controller.getManager().checkEMBOSSPath(getEMBOSSPath());
	}

	protected boolean isValidNCBIPath() {
		return this.controller.getManager().checkNCBIPath(getNCBIPath());
	}
	
	protected void buildRepository() {
		try {
			this.controller.getEnvironment()
				.getRepositoryPaths()
			.buildBaseDirectory(this.getRepositoryDirectory());
			
			this.btnBuildRepository.setEnabled(false);
			JOptionPane.showMessageDialog(
				ConfigurationPanel.this, 
				"Repository structure was correctly built.",
				"Repository Built",
				JOptionPane.INFORMATION_MESSAGE
			);
			
			this.fireChange();
		} catch (Exception e) {
			this.btnBuildRepository.setEnabled(true);
			
			JOptionPane.showMessageDialog(
				ConfigurationPanel.this, 
				"Error building repository. Please, check path and press 'Build' or change path",
				"Repository Building Error",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	public PathsConfiguration getConfiguration() {
		if (this.isValidRepositoryPath() && this.isValidBLASTPath()) {
			final String blastPath = this.getBLASTPath();
			final String embossPath = this.getEMBOSSPath();
			final String ncbiPath = this.getNCBIPath();
			
			return new PathsConfiguration(
				this.getRepositoryDirectory(), 
				blastPath == null ? null : new File(blastPath),
				embossPath == null ? null : new File(embossPath),
				ncbiPath == null ? null : new File(ncbiPath)
			);
		} else {
			return null;
		}
	}
	
	private final class SystemPathSelectionActionListener implements
			ActionListener {
		private final JTextField txtAssociated;
		private final Callable<Boolean> callback;

		private SystemPathSelectionActionListener(JTextField txtAssociated, Callable<Boolean> callback) {
			this.txtAssociated = txtAssociated;
			this.callback = callback;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final String previousPath = this.txtAssociated.getText();
			this.txtAssociated.setText("");
			
			try {
				if (this.callback.call()) {
					ConfigurationPanel.this.fireChange();
				} else {
					txtAssociated.setText(previousPath);
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private final class PathSelectionActionListener implements ActionListener {
		private final JTextField txtAssociated;
		private final Callable<Boolean> callback;

		private PathSelectionActionListener(JTextField txtAssociated, Callable<Boolean> callback) {
			this.txtAssociated = txtAssociated;
			this.callback = callback;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final JFileChooser chooser = new JFileChooser(
				new File(txtAssociated.getText())
			);
			
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			
			if (chooser.showOpenDialog(ConfigurationPanel.this) == JFileChooser.APPROVE_OPTION) {
				final String previousPath = txtAssociated.getText();
				txtAssociated.setText(chooser.getSelectedFile().getAbsolutePath());
				
				try {
					if (this.callback.call()) {
						ConfigurationPanel.this.fireChange();
					} else {
						txtAssociated.setText(previousPath);
					}
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		}
	}

	public static class ConfigurationChangeEvent extends EventObject {
		private static final long serialVersionUID = 1L;
		
		private final PathsConfiguration configuration;

		protected ConfigurationChangeEvent(ConfigurationPanel panel) {
			this(panel, panel.getConfiguration());
		}
		
		public ConfigurationChangeEvent(Object source, PathsConfiguration configuration) {
			super(source);
			this.configuration = configuration;
		}
		
		public PathsConfiguration getConfiguration() {
			return configuration;
		}
	}
	
	public static interface ConfigurationChangeEventListener extends EventListener {
		public void configurationChanged(ConfigurationChangeEvent event);
	}
}
