package es.uvigo.esei.sing.bdbm;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.SwingUtilities;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.Clipboard;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.Lifecycle;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.tree.AIBenchJTreeManager;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.datatypes.NucleotideDatabaseWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.NucleotideExportWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.NucleotideFastaWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.NucleotideSearchEntryWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.ProteinDatabaseWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.ProteinExportWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.ProteinFastaWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.ProteinSearchEntryWrapper;
import es.uvigo.esei.sing.bdbm.datatypes.SequenceEntityWrapper;
import es.uvigo.esei.sing.bdbm.persistence.BDBMRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryEvent;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryListener;
import es.uvigo.esei.sing.bdbm.utils.CustomClipboardTreeModel;

public class BDBMLifecycle extends Lifecycle {
	private final static Class<?>[] PROTEIN_CLIPBOARD_ORDER = new Class<?>[] { 
		ProteinFastaWrapper.class,
		ProteinDatabaseWrapper.class, 
		ProteinSearchEntryWrapper.class,
		ProteinExportWrapper.class
	};
	private final static Class<?>[] NUCLEOTIDE_CLIPBOARD_ORDER = new Class<?>[] { 
		NucleotideFastaWrapper.class,
		NucleotideDatabaseWrapper.class, 
		NucleotideSearchEntryWrapper.class,
		NucleotideExportWrapper.class
	};
	
	private final static Map<Class<?>, String> NUCLEOTIDE_CLIPBOARD_MAPPING = 
		new HashMap<Class<?>, String>(BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_ORDER.length+1, 1f);
	private final static Map<Class<?>, String> PROTEIN_CLIPBOARD_MAPPING = 
		new HashMap<Class<?>, String>(BDBMLifecycle.PROTEIN_CLIPBOARD_ORDER.length+1, 1f);
	
	static {
		BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_MAPPING.put(BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_ORDER[0], "Fasta files");
		BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_MAPPING.put(BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_ORDER[1], "Databases");
		BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_MAPPING.put(BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_ORDER[2], "Search Entries");
		BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_MAPPING.put(BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_ORDER[3], "Exports");
		BDBMLifecycle.PROTEIN_CLIPBOARD_MAPPING.put(BDBMLifecycle.PROTEIN_CLIPBOARD_ORDER[0], "Fasta files");
		BDBMLifecycle.PROTEIN_CLIPBOARD_MAPPING.put(BDBMLifecycle.PROTEIN_CLIPBOARD_ORDER[1], "Databases");
		BDBMLifecycle.PROTEIN_CLIPBOARD_MAPPING.put(BDBMLifecycle.PROTEIN_CLIPBOARD_ORDER[2], "Search Entries");
		BDBMLifecycle.PROTEIN_CLIPBOARD_MAPPING.put(BDBMLifecycle.PROTEIN_CLIPBOARD_ORDER[3], "Exports");
	}
	
	private static final class AIBenchRepositoryListener implements RepositoryListener {
		private final Clipboard clipboard;
		private final BDBMController controller;
		
		public AIBenchRepositoryListener(BDBMController controller) {
			this.clipboard = Core.getInstance().getClipboard();
			this.controller = controller;
		}
		
		@Override
		public void repositoryChanged(RepositoryEvent event) {
			final SequenceEntity entity = event.getEntity();
			
			switch (event.getType()) {
			case CREATE:
				if (!this.clipboardContains(entity))
					this.clipboard.putItem(SequenceEntityWrapper.wrap(entity), entity.getName());
				break;
			case DELETE:
				if (!this.controller.exists(entity)) {
					final ClipboardItem ci = findClipboardItemFor(entity);
					
					if (ci != null) {
						this.clipboard.removeClipboardItem(ci);
					}
				}
				break;
			}
		}
		
		protected boolean clipboardContains(final SequenceEntity entity) {
			return this.findClipboardItemFor(entity) != null;
		}

		protected ClipboardItem findClipboardItemFor(final SequenceEntity entity) {
			for (ClipboardItem item : clipboard.getItemsByClass(SequenceEntityWrapper.class)) {
				if (((SequenceEntityWrapper<?>) item.getUserData()).getWrapped().equals(entity)) {
					return item;
				}
			}
			return null;
		}
	}
	
	@Override
	public void start() {
		if (System.getProperty("aibench.nogui") != null) return;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final Workbench workbench = Workbench.getInstance();
				
				while (workbench.getMainFrame() == null) {
					Thread.yield();
				}
				
				final AIBenchJTreeManager nuclTreeManager = workbench.getTreeManager();
				final AIBenchJTreeManager protTreeManager = new AIBenchJTreeManager();
				
				final JTree treeNucl = nuclTreeManager.getAIBenchClipboardTree();
				final JTree treeProt = protTreeManager.getAIBenchClipboardTree();
				workbench.putItemInSlot("left", "Protein", "clipboard.tree.protein", treeProt);
				
				final CustomClipboardTreeModel treeNuclModel = new CustomClipboardTreeModel(
					BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_ORDER, 
					BDBMLifecycle.NUCLEOTIDE_CLIPBOARD_MAPPING, true, true
				);
				
				final CustomClipboardTreeModel protNuclModel = new CustomClipboardTreeModel(
					BDBMLifecycle.PROTEIN_CLIPBOARD_ORDER, 
					BDBMLifecycle.PROTEIN_CLIPBOARD_MAPPING, true, true
				);
//				for (Class<?> clazz : CLIPBOARD_ORDER) {
//					treeModel.addAutoShow(clazz);
//				}
//				treeModel.addAutoShow(Project.class);
//				treeModel.addAutoShow(BatchProject.class);
				
				treeNucl.setModel(treeNuclModel);
				treeProt.setModel(protNuclModel);
//				
//				ClipboardViewsMouseListener.setAsDefaultClipboardTreeListener();
				
				new Thread() {
					public void run() {

						final BDBMRepositoryManager repositoryManager = BDBM.getInstance().getRepositoryManager();
						final BDBMController controller = BDBM.getInstance().getController();
						final Clipboard clipboard = Core.getInstance().getClipboard();
						final AIBenchRepositoryListener repositoryListener = 
							new AIBenchRepositoryListener(controller);
						
						loadEntities(clipboard, controller.listProteinDatabases());
						loadEntities(clipboard, controller.listNucleotideDatabases());
						repositoryManager.database().addRepositoryListener(repositoryListener);
						
						loadEntities(clipboard, controller.listProteinFastas());
						loadEntities(clipboard, controller.listNucleotideFastas());
						repositoryManager.fasta().addRepositoryListener(repositoryListener);
						
						loadEntities(clipboard, controller.listProteinSearchEntries());
						loadEntities(clipboard, controller.listNucleotideSearchEntries());
						repositoryManager.searchEntry().addRepositoryListener(repositoryListener);
						
						loadEntities(clipboard, controller.listProteinExports());
						loadEntities(clipboard, controller.listNucleotideExports());
						repositoryManager.export().addRepositoryListener(repositoryListener);
					};
				}.start();
			}
		});
	}

	protected <T extends SequenceEntity> void loadEntities(final Clipboard clipboard, final T[] entities) {
		for (T entity : entities) {
			clipboard.putItem(SequenceEntityWrapper.wrap(entity), entity.getName());
		}
	}
}
