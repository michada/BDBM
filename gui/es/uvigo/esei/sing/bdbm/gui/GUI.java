package es.uvigo.esei.sing.bdbm.gui;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import es.uvigo.esei.sing.bdbm.BDBMManager;
import es.uvigo.esei.sing.bdbm.controller.DefaultBDBMController;
import es.uvigo.esei.sing.bdbm.environment.BDBMEnvironment;
import es.uvigo.esei.sing.bdbm.environment.DefaultBDBMEnvironment;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.BinaryCheckException;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.execution.NCBIBinaryToolsFactoryBuilder;
import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;
import es.uvigo.esei.sing.bdbm.gui.configuration.PathsConfiguration;
import es.uvigo.esei.sing.bdbm.persistence.DefaultBDBMRepositoryManager;

public class GUI implements Observer {
	private final static ImageIcon IMAGE_BDBM = new ImageIcon(BDBMSplash.class.getResource("images/bdbm.png"));
	
	private BDBMManager manager;
	private BDBMGUIController guiController;

	private JFrame mainFrame;
	
	public GUI() {}
	
	public GUI(boolean init) throws FileNotFoundException, IOException, BinaryCheckException {
		if (init) {
			this.initControllers();
			this.initGUI();
		}
	}

	private void initControllers() throws FileNotFoundException, IOException, BinaryCheckException {
		if (this.guiController != null)
			this.guiController.deleteObserver(this);
		
		try {
			this.manager = this.createBDBMManager();
			this.guiController = createGUIController(this.manager);
			this.guiController.addObserver(this);
		} catch (Exception e) {
			this.manager = null;
			this.guiController = null;
			
			throw e;
		}
	}
	
	private void initGUI() throws FileNotFoundException, IOException, BinaryCheckException {
		if (this.manager == null || this.guiController == null)
			this.initControllers();
		
		this.mainFrame = this.createMainFrame(
			this.createMainPanel(this.guiController)
		);
	}
	
	public void showMainFrame() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GUI.this.mainFrame.setVisible(true);
			}
		});
	}
	
	public void showMainFrameAndWait()
	throws InterruptedException, InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				GUI.this.mainFrame.setVisible(true);
			}
		});
	}
	
	private BDBMManager createBDBMManager()
	throws FileNotFoundException, IOException, BinaryCheckException {
		return new BDBMManager(
			createBDBMEnvironment(),
			createBDBMRepositoryManager(),
			createBDBMController()
		);
	}

	private DefaultBDBMEnvironment createBDBMEnvironment()
	throws FileNotFoundException, IOException, IllegalStateException {
		return new DefaultBDBMEnvironment(new File("bdbm.conf"));
	}

	private DefaultBDBMRepositoryManager createBDBMRepositoryManager() {
		return new DefaultBDBMRepositoryManager();
	}

	private DefaultBDBMController createBDBMController() {
		return new DefaultBDBMController();
	}
	
	private BDBMGUIController createGUIController(BDBMManager manager) {
		return new BDBMGUIController(manager);
	}
	
	private BDBMMainPanel createMainPanel(BDBMGUIController controller) {
		return new BDBMMainPanel(controller);
	}
	
	private void shutdown() {
		if (this.manager != null) {
			this.manager.shutdown();
		}
		if (this.mainFrame != null) {
			this.mainFrame.setVisible(false);
			this.mainFrame.dispose();
		}
		
		this.manager = null;
		this.guiController = null;
		this.mainFrame = null;
	}
	
	private JFrame createMainFrame(BDBMMainPanel mainPanel) {
		final JFrame frame = new JFrame("BLAST DataBase Manager");
		frame.setIconImage(GUI.IMAGE_BDBM.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(960, 700);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(mainPanel);
		frame.setJMenuBar(mainPanel.createMenuBar());
		
		return frame;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new NimbusLookAndFeel());
					
					final BDBMSplash splash = new BDBMSplash();
					try {
						splash.setVisible(true);
			
						final GUI gui = new GUI();
						final DefaultBDBMEnvironment env = gui.createBDBMEnvironment();
			
						if (!checkRepositoryPaths(env, splash)) {
							System.exit(10);
						} else if (!checkBlastBinaries(env, splash)) {
							System.exit(11);
						} else if (!checkEmbossBinaries(env, splash)) {
							System.exit(12);
						} else if (!checkNcbiBinaries(env, splash)) {
							System.exit(13);
						} else {
							gui.initGUI();
							gui.showMainFrame();
						}
					} catch (FileNotFoundException fnfe) {
						JOptionPane.showMessageDialog(null,
							"Configuration file not found in path: "
								+ new File("bdbm.conf").getAbsolutePath()
								+ ". Program will exit.",
							"Missing Configuration File",
							JOptionPane.ERROR_MESSAGE
						);
			
						System.exit(1);
					} catch (IllegalStateException ise) {
						JOptionPane.showMessageDialog(null, 
							ise.getMessage() + ". Program will exit.",
							"Missing Property",
							JOptionPane.ERROR_MESSAGE
						);
			
						System.exit(2);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, 
							"Unknown error: " + e.getMessage() + ". Program will exit.",
							"Initialization Error",
							JOptionPane.ERROR_MESSAGE
						);
			
						System.exit(-1);
					} finally {
						splash.setVisible(false);
					}
				} catch (Exception e) {}
			}
		});
	}

	private static boolean checkEmbossBinaries(final DefaultBDBMEnvironment env, final Component parent) throws IOException {
		final JFileChooser chooser = new JFileChooser(env.getEMBOSSBinaries().getBaseDirectory());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		
		while (!checkEmbossPath(env)) {
			if (askForEmbossPath(parent) && 
				chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION
			) {
				final File EmbossPath = chooser.getSelectedFile();
				try {
					env.changeEMBOSSPath(EmbossPath, false);
				} catch (IOException ioe) {
					showIOError(parent, ioe);
				}
			} else {
				return false;
			}
		}
		
		env.saveToProperties();
		
		return true;
	}

	private static boolean checkEmbossPath(final BDBMEnvironment env) {
		try {
			EMBOSSBinaryToolsFactoryBuilder.newFactory(env.getEMBOSSBinaries());
			
			return true;
		} catch (BinaryCheckException bbce) {
			return false;
		}
	}

	private static boolean askForEmbossPath(final Component parent) {
		return JOptionPane.showConfirmDialog(
			parent, 
			"Missing or invalid EMBOSS binaries path. Do you want to select a new path?\n"
			+ "(If you select 'No', program will exit)",
			"Invalid EMBOSS",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.ERROR_MESSAGE
		) == JOptionPane.YES_OPTION;
	}

	private static boolean checkBlastBinaries(final DefaultBDBMEnvironment env, final Component parent) throws IOException {
		final JFileChooser chooser = new JFileChooser(env.getBLASTBinaries().getBaseDirectory());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		
		while (!checkBlastPath(env)) {
			if (askForBlastPath(parent) && 
				chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION
			) {
				final File blastPath = chooser.getSelectedFile();
				try {
					env.changeBLASTPath(blastPath, false);
				} catch (IOException ioe) {
					showIOError(parent, ioe);
				}
			} else {
				return false;
			}
		}
		
		env.saveToProperties();
		
		return true;
	}

	private static boolean checkBlastPath(final BDBMEnvironment env) {
		try {
			BLASTBinaryToolsFactoryBuilder.newFactory(env.getBLASTBinaries());
			
			return true;
		} catch (BinaryCheckException bbce) {
			return false;
		}
	}

	private static boolean askForBlastPath(final Component parent) {
		return JOptionPane.showConfirmDialog(
			parent, 
			"Missing or invalid BLAST binaries path. Do you want to select a new path?\n"
			+ "(If you select 'No', program will exit)",
			"Invalid BLAST",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.ERROR_MESSAGE
		) == JOptionPane.YES_OPTION;
	}

	private static boolean checkNcbiBinaries(DefaultBDBMEnvironment env, Component parent)
	throws IOException {
		final JFileChooser chooser = new JFileChooser(env.getNCBIBinaries().getBaseDirectory());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		
		while (!checkNcbiPath(env)) {
			if (askForNcbiPath(parent) && 
				chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION
			) {
				final File ncbiPath = chooser.getSelectedFile();
				try {
					env.changeNCBIPath(ncbiPath, false);
				} catch (IOException ioe) {
					showIOError(parent, ioe);
				}
			} else {
				return false;
			}
		}
		
		env.saveToProperties();
		
		return true;
	}

	private static boolean checkNcbiPath(final BDBMEnvironment env) {
		try {
			NCBIBinaryToolsFactoryBuilder.newFactory(env.getNCBIBinaries());
			
			return true;
		} catch (BinaryCheckException bbce) {
			return false;
		}
	}

	private static boolean askForNcbiPath(final Component parent) {
		return JOptionPane.showConfirmDialog(
			parent, 
			"Missing or invalid NCBI binaries path. Do you want to select a new path?\n"
			+ "(If you select 'No', program will exit)",
			"Invalid NCBI",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.ERROR_MESSAGE
		) == JOptionPane.YES_OPTION;
	}

	private static boolean checkRepositoryPaths(final DefaultBDBMEnvironment env, final Component parent) {
		final RepositoryPaths repositoryPaths = env.getRepositoryPaths();
		if (repositoryPaths.checkBaseDirectory(repositoryPaths.getBaseDirectory())) {
			return true;
		} else {
			final JFileChooser chooser = new JFileChooser(repositoryPaths.getBaseDirectory());
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			
			while (!repositoryPaths.checkBaseDirectory(repositoryPaths.getBaseDirectory())) {
				if (askForRepositoryPath(parent) && 
					chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION
				) {
					final File repositoryPath = chooser.getSelectedFile();
					
					try {
						if (repositoryPaths.checkBaseDirectory(repositoryPath)) {
							env.changeRepositoryPath(repositoryPath);
						} else if (repositoryPath.canWrite()) {
							if (askForCreatingRepositoryIn(parent, repositoryPath)) {
								repositoryPaths.buildBaseDirectory(repositoryPath);
								env.changeRepositoryPath(repositoryPath);
							}
						}
					} catch (IOException ioe) {
						showIOError(parent, ioe);
					}
				} else {
					return false;
				}
			}
			
			return true;
		}
	}

	private static void showIOError(final Component parent, IOException ioe) {
		JOptionPane.showMessageDialog(
			parent, 
			"I/O error: " + ioe.getMessage(),
			"I/O Error",
			JOptionPane.ERROR_MESSAGE
		);
	}

	private static boolean askForCreatingRepositoryIn(final Component parent, final File repositoryPath) {
		return JOptionPane.showConfirmDialog(
			parent, 
			"Selected path does not contain a valid repository. Do you want to create a repository in " + repositoryPath.getAbsolutePath() + "?",
			"Create Base Repository",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		) == JOptionPane.YES_OPTION;
	}

	private static boolean askForRepositoryPath(final Component parent) {
		return JOptionPane.showConfirmDialog(
			parent, 
			"Missing or invalid repository path. Do you want to select a new path?\n"
			+ "(If you select 'No', program will exit)",
			"Invalid Repository",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.ERROR_MESSAGE
		) == JOptionPane.YES_OPTION;
	}
	
	private final class RestartTask implements Runnable {
		@Override
		public void run() {
			GUI.this.shutdown();
			
			final BDBMSplash splash = new BDBMSplash();
			splash.setVisible(true);
			
			try {
				GUI.this.initGUI();
				
				GUI.this.showMainFrameAndWait();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, 
					"Error produced while restarting: " + e.getMessage() + ". Program will exit.",
					"Missing Property",
					JOptionPane.ERROR_MESSAGE
				);
				
				System.exit(15);
			} finally {
				splash.setVisible(false);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof PathsConfiguration) {
			if (SwingUtilities.isEventDispatchThread()) {
				new Thread(new RestartTask()).start();
			} else {
				new RestartTask().run();
			}
		}
	}
}
