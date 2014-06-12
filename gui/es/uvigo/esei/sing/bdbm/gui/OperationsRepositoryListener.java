package es.uvigo.esei.sing.bdbm.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import es.uvigo.ei.sing.yacli.Command;
import es.uvigo.ei.sing.yacli.DefaultParameters;
import es.uvigo.ei.sing.yacli.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.Option;
import es.uvigo.ei.sing.yacli.ParameterValue;
import es.uvigo.ei.sing.yacli.Parameters;
import es.uvigo.ei.sing.yacli.SingleParameterValue;
import es.uvigo.esei.sing.bdbm.cli.commands.BLASTDBAliasToolCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.GetORFCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.MakeBLASTDBCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.RetrieveSearchEntryCommand;
import es.uvigo.esei.sing.bdbm.cli.commands.converters.FileOption;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.gui.command.BDBMCommandAction;
import es.uvigo.esei.sing.bdbm.gui.command.CommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.dialogs.BLASTDBAliasToolCommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.dialogs.GetORFCommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.dialogs.MakeBLASTDBCommandDialog;
import es.uvigo.esei.sing.bdbm.gui.command.dialogs.RetrieveSearchEntryCommandDialog;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public class OperationsRepositoryListener extends MouseAdapter {
	private final BDBMGUIController controller;
	
	public OperationsRepositoryListener(BDBMGUIController controller) {
		this.controller = controller;
	}

	protected static Parameters singletonParameters(
		Option<?> option, String value
	) {
		final Map<Option<?>, ParameterValue<?>> values = 
			new HashMap<Option<?>, ParameterValue<?>>();
		
		values.put(option, new SingleParameterValue(value));
		
		return new DefaultParameters(values, false);
	}

	protected static Parameters singletonParameters(
		Option<?> option, List<String> value
	) {
		final Map<Option<?>, ParameterValue<?>> values = 
			new HashMap<Option<?>, ParameterValue<?>>();
		
		values.put(option, new MultipleParameterValue(value));
		
		return new DefaultParameters(values, false);
	}
	
	protected Map<Option<?>, ParameterValue<?>> createEntityParams(
		SequenceEntity entity,
		Option<SequenceType> typeOption,
		FileOption fileOption
	) {
		final Map<Option<?>, ParameterValue<?>> parameters = new HashMap<>();
		
		parameters.put(
			typeOption,
			new SingleParameterValue(entity.getType().name())
		);
		parameters.put(
			fileOption,
			new SingleParameterValue(entity.getBaseFile().getAbsolutePath())
		);
		
		return parameters;
	}
	
	protected BDBMCommandAction createBDBMCommandAction(
		Class<? extends Command> commandClass,
		Class<? extends CommandDialog> commandDialogClass,
		Parameters parameters
	) {
		try {
			final BDBMController bdbmController = this.controller.getController();
			
			return new BDBMCommandAction(
				bdbmController,
				commandClass.getConstructor(BDBMController.class).newInstance(bdbmController),
				commandDialogClass,
				parameters
			);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid command class " + commandClass.getName(), e);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (e.getSource() instanceof JTree) {
				final JTree tree = (JTree) e.getSource();
				
				final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				tree.setSelectionPath(path);
				
				if (path.getLastPathComponent() instanceof DefaultMutableTreeNode) {
					final DefaultMutableTreeNode node = 
						(DefaultMutableTreeNode) path.getLastPathComponent();
					
					if (node.getUserObject() instanceof Fasta) {
						final Fasta fasta = (Fasta) node.getUserObject();
						
						final Action[] actions;
						if (fasta instanceof NucleotideFasta) {
							actions = new Action[] {
								this.createBDBMCommandAction(
									MakeBLASTDBCommand.class, 
									MakeBLASTDBCommandDialog.class,
									new DefaultParameters(createEntityParams(
										fasta,
										MakeBLASTDBCommand.OPTION_DB_TYPE,
										MakeBLASTDBCommand.OPTION_INPUT
									))
								),
								this.createBDBMCommandAction(
									GetORFCommand.class, 
									GetORFCommandDialog.class, 
									singletonParameters(
										GetORFCommand.OPTION_FASTA, 
										fasta.getFile().getAbsolutePath()
									)
								)
							};
						} else {
							actions = new Action[] {
								this.createBDBMCommandAction(
									MakeBLASTDBCommand.class, 
									MakeBLASTDBCommandDialog.class,
									new DefaultParameters(createEntityParams(
										fasta,
										MakeBLASTDBCommand.OPTION_DB_TYPE,
										MakeBLASTDBCommand.OPTION_INPUT
									))
								)
							};
						}
						
						this.showPopupMenu(
							"Fasta", 
							"Fasta", 
							tree, 
							fasta, 
							e.getX(), 
							e.getY(),
							actions
						);
					} else if (node.getUserObject() instanceof Database) {
						final Database database = (Database) node.getUserObject();
						
						final BDBMCommandAction retrieveCA;

						this.showPopupMenu(
							"Database", 
							"Database", 
							tree, 
							database, 
							e.getX(), 
							e.getY(), 
							this.createBDBMCommandAction(
								BLASTDBAliasToolCommand.class, 
								BLASTDBAliasToolCommandDialog.class,
								singletonParameters(
									BLASTDBAliasToolCommand.OPTION_DATABASES, 
									Arrays.asList(database.getBaseFile().getAbsolutePath())
								)
							),
							retrieveCA = this.createBDBMCommandAction(
								RetrieveSearchEntryCommand.class,
								RetrieveSearchEntryCommandDialog.class,
								new DefaultParameters(createEntityParams(
									database, 
									RetrieveSearchEntryCommand.OPTION_DB_TYPE, 
									RetrieveSearchEntryCommand.OPTION_DATABASE
								))
							)
						);
						
						retrieveCA.addParamValue(boolean.class, this.controller.isAccessionInferEnabled());
					} else if (node.getUserObject() instanceof SearchEntry) {
						final SearchEntry searchEntry = (SearchEntry) node.getUserObject();
						
						this.showPopupMenu(
							"Search Entry", "Search Entry", tree, searchEntry, 
							e.getX(), e.getY()
						);
					} else if (node.getUserObject() instanceof Export) {
						final Export export = (Export) node.getUserObject();
						
						this.showPopupMenu(
							"Export", "Export", tree, export, 
							e.getX(), e.getY()
						);
					}
				}
			}
		}
	}
	
	protected void showPopupMenu(
		String title, 
		String entityName, 
		Component parent, 
		SequenceEntity entity,
		int x, int y,
		Action ... additionalActions
	) {
		final JPopupMenu menu = new JPopupMenu(title);
		final JMenuItem itemTitle = new JMenuItem(title);
		itemTitle.setEnabled(false);
		itemTitle.setFont(itemTitle.getFont().deriveFont(Font.BOLD));
		itemTitle.setForeground(Color.BLACK);
		menu.add(itemTitle);
		menu.addSeparator();
		
		if (additionalActions.length > 0) {
			for (Action additionalAction : additionalActions) {
				menu.add(additionalAction);
			}
			
			menu.addSeparator();
		}

		menu.add(new DeleteAction(
			this.controller.getController(), 
			SwingUtilities.getWindowAncestor(parent), 
			entity, 
			entityName
		));
		
		menu.show(parent, x, y);
	}
	
	protected static class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		private final BDBMController controller;
		private final Component parent;
		private final SequenceEntity sequenceEntity;
		private final String entityName;

		private DeleteAction(
			BDBMController controller, 
			Component parent, 
			SequenceEntity sequenceEntity, 
			String entityName
		) {
			super("Delete " + entityName.toLowerCase());
			
			this.controller = controller;
			this.parent = parent;
			this.sequenceEntity = sequenceEntity;
			this.entityName = entityName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final String lowerEntityName = this.entityName.toLowerCase();
			
			if (JOptionPane.showConfirmDialog(
				parent, 
				this.entityName + " will be deleted. Do you want to continue?",
				"Delete " + lowerEntityName,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			) == JOptionPane.YES_OPTION) {
				try {
					this.controller.delete(sequenceEntity);
					JOptionPane.showMessageDialog(
						this.parent, 
						this.entityName + " deleted",
						"Delete " + lowerEntityName,
						JOptionPane.INFORMATION_MESSAGE
					);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(
						parent, 
						"Error deleting " + lowerEntityName,
						"Delete " + lowerEntityName,
						JOptionPane.ERROR_MESSAGE
					);
				}
			}
		}
	}
}
