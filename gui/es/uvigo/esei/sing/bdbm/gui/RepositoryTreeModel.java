package es.uvigo.esei.sing.bdbm.gui;

import java.io.File;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.BDBMRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public class RepositoryTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = 1L;

	private final BDBMRepositoryManager repositoryManager;
	
	private final DefaultMutableTreeNode tnFasta;
	private final DefaultMutableTreeNode tnDatabase;
	private final DefaultMutableTreeNode tnSearchEntry;
	private final DefaultMutableTreeNode tnExport;
	
	public RepositoryTreeModel(
		SequenceType sequenceType,
		BDBMRepositoryManager repositoryManager
	) {
		super(createTreeNodes(sequenceType, repositoryManager));
		this.repositoryManager = repositoryManager;
		
		this.tnFasta = (DefaultMutableTreeNode) this.getChild(this.getRoot(), 0);
		this.tnDatabase = (DefaultMutableTreeNode) this.getChild(this.getRoot(), 1);
		this.tnSearchEntry = (DefaultMutableTreeNode) this.getChild(this.getRoot(), 2);
		this.tnExport = (DefaultMutableTreeNode) this.getChild(this.getRoot(), 3);
		
		this.repositoryManager.fasta().addRepositoryListener(
			new SynchronizationRepositoryListener(this, sequenceType, this.tnFasta)
		);
		this.repositoryManager.database().addRepositoryListener(
			new SynchronizationRepositoryListener(this, sequenceType, this.tnDatabase)
		);
		this.repositoryManager.searchEntry().addRepositoryListener(
			new SynchronizationRepositoryListener(this, sequenceType, this.tnSearchEntry)
		);
		this.repositoryManager.export().addRepositoryListener(
			new SynchronizationRepositoryListener(this, sequenceType, this.tnExport)
		);
	}
	
	void insertNode(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode child, int childIndex) {
		if (childIndex < 0) {
			parentNode.add(child);
			childIndex = parentNode.getChildCount() - 1;
		} else {
			parentNode.insert(child, childIndex);
		}

		this.fireTreeNodesInserted(
			parentNode, parentNode.getPath(),
			new int[] { childIndex }, 
			new Object[] { child }
		);
	}
	
	void insertNode(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode child) {
		this.insertNode(parentNode, child, -1);
	}
	
	void deleteNode(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode child) {
		final int childIndex = parentNode.getIndex(child);
		
		parentNode.remove(childIndex);
		
		this.fireTreeNodesRemoved(
			parentNode, parentNode.getPath(),
			new int[] { childIndex },
			new Object[] { child }
		);
	}
	
	void replaceNode(DefaultMutableTreeNode oldChild, DefaultMutableTreeNode newChild) {
		if (oldChild.getParent() instanceof DefaultMutableTreeNode) {
			final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) oldChild.getParent();
			final int childIndex = parent.getIndex(oldChild);
			
			parent.remove(oldChild);
			parent.insert(newChild, childIndex);
			
			this.fireTreeNodesChanged(
				parent, parent.getPath(), 
				new int[] { childIndex }, new Object[] { newChild }
			);
		} else {
			throw new IllegalArgumentException("old node must have a DefaultMutableTreeNode parent");
		}
	}
	
	private static MutableTreeNode createTreeNodes(
		SequenceType sequenceType,
		BDBMRepositoryManager repositoryManager
	) {
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		
		root.add(createFastaNodes(
			repositoryManager.fasta().list(sequenceType)
		));
		
		root.add(createDatabaseNodes(
			repositoryManager.database().list(sequenceType)
		));
		
		root.add(createSearchEntry(
			repositoryManager.searchEntry().list(sequenceType)	
		));

		root.add(createExport(
			repositoryManager.export().list(sequenceType)
		));
		
		return root;
	}

	private static MutableTreeNode createFastaNodes(Fasta[] fastas) {
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Fasta Files");
		
		for (Fasta fasta : fastas) {
			root.add(createFastaNode(fasta));
		}
		
		return root;
	}
	
	private static MutableTreeNode createDatabaseNodes(Database[] databases) {
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Databases");
		
		for (Database database : databases) {
			root.add(createDatabaseNode(database));
		}
		
		return root;
	}
	
	private static MutableTreeNode createSearchEntry(SearchEntry[] entries) {
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Search Entries");
		
		for (SearchEntry entry : entries) {
			root.add(createSearchEntryNode(entry));
		}
		
		return root;
	}
	
	private static MutableTreeNode createExport(Export[] exports) {
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Exports");
		
		for (Export export : exports) {
			root.add(createExportNode(export));
		}
		
		return root;
	}

	static DefaultMutableTreeNode createSequenceEntityNode(SequenceEntity entity) 
	throws IllegalArgumentException {
		if (entity instanceof Fasta) {
			return createFastaNode((Fasta) entity);
		} else if (entity instanceof Database) {
			return createDatabaseNode((Database) entity);
		} else if (entity instanceof SearchEntry) {
			return createSearchEntryNode((SearchEntry) entity);
		} else if (entity instanceof Export) {
			return createExportNode((Export) entity);
		} else {
			throw new IllegalArgumentException("Unknown entity type");
		}
	}
	
	private static DefaultMutableTreeNode createFastaNode(Fasta fasta) {
		return new TextFileMutableTreeObject(fasta, fasta.getFile(), RepositoryTreeRenderer.ICON_FASTA);
	}
	
	private static DefaultMutableTreeNode createDatabaseNode(Database database) {
		return new DefaultMutableTreeNode(database);
	}
	
	private static DefaultMutableTreeNode createSearchEntryNode(SearchEntry entry) {
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry);
		
		for (SearchEntry.Query query : entry.listQueries()) {
			node.add(createSearchEntryQuery(query));
		}
		
		return node;
	}

	static TextFileMutableTreeObject createSearchEntryQuery(SearchEntry.Query query) {
		return new TextFileMutableTreeObject(query, query.getBaseFile(), RepositoryTreeRenderer.ICON_SEARCH_ENTRY);
	}

	private static DefaultMutableTreeNode createExportNode(Export export) {
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(export);
		
		for (Export.ExportEntry exportEntry : export.listEntries()) {
			node.add(createExportEntryNode(exportEntry));
		}
		
		return node;
	}

	static DefaultMutableTreeNode createExportEntryNode(Export.ExportEntry exportEntry) {
		final DefaultMutableTreeNode nodeExportEntry = new DefaultMutableTreeNode(exportEntry);
		
		nodeExportEntry.add(createExportEntryOutFileNode(exportEntry.getOutFile()));
		
		if (exportEntry.getSummaryFastaFile().canRead()) {
			nodeExportEntry.add(createExportEntrySummaryFileNode(exportEntry.getSummaryFastaFile()));
		}
		
		for (File txtFile : exportEntry.getSequenceFiles()) {
			nodeExportEntry.add(createExportEntryTextFileNode(txtFile));
		}
		return nodeExportEntry;
	}
	
	static DefaultMutableTreeNode createExportEntryTextFileNode(File exportEntryFile) {
		return new TextFileMutableTreeObject(exportEntryFile, RepositoryTreeRenderer.ICON_EXPORT);
	}
	
	static DefaultMutableTreeNode createExportEntryOutFileNode(File exportEntryFile) {
		return new TextFileMutableTreeObject(exportEntryFile, RepositoryTreeRenderer.ICON_EXPORT);
	}
	
	static DefaultMutableTreeNode createExportEntrySummaryFileNode(File exportEntryFile) {
		return new TextFileMutableTreeObject(exportEntryFile, RepositoryTreeRenderer.ICON_EXPORT);
	}
	
	public static class TextFileMutableTreeObject extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;

		private final File file;
		private final Icon icon;
		
		public TextFileMutableTreeObject(File file) {
			this(file.getName(), file, null);
		}
		
		public TextFileMutableTreeObject(File file, Icon icon) {
			this(file.getName(), file, icon);
		}
		
		public TextFileMutableTreeObject(Object value, File file) {
			this(value, file, null);
		}
		
		public TextFileMutableTreeObject(Object value, File file, Icon icon) {
			super(value);
			
			this.file = file;
			this.icon = icon;
		}
		
		public File getFile() {
			return this.file;
		}
		
		public Icon getIcon() {
			return this.icon;
		}
	}
}
