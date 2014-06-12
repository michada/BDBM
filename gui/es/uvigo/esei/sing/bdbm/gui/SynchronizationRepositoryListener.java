package es.uvigo.esei.sing.bdbm.gui;

import java.io.File;
import java.util.Arrays;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export.ExportEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryEvent;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryListener;

class SynchronizationRepositoryListener implements RepositoryListener {
	private final SequenceType sequenceType;
	private final RepositoryTreeModel treeModel;
	private final DefaultMutableTreeNode node;

	public SynchronizationRepositoryListener(
		RepositoryTreeModel treeModel, 
		SequenceType sequenceType, 
		DefaultMutableTreeNode node
	) {
		this.treeModel = treeModel;
		this.sequenceType = sequenceType;
		this.node = node;
	}
	
	@Override
	public void repositoryChanged(final RepositoryEvent event) {
		if (event.getEntity().getType() == this.sequenceType) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateTree(event);
				}
			});
		}
	}

	private synchronized void updateTree(RepositoryEvent event) {
		final SequenceEntity entity = event.getEntity();
		final DefaultMutableTreeNode entityNode = 
			this.getUserObjectChildNode(this.node, entity);
		
		switch (event.getType()) {
		case CREATE:
			if (entityNode == null) {
				this.treeModel.insertNode(
					this.node, 
					RepositoryTreeModel.createSequenceEntityNode(entity)
				);
			}
			
			break;
		case INVALIDATED:
		case DELETE:
			if (entityNode != null) {
				this.treeModel.deleteNode(this.node, entityNode);
			}
			
			break;
		case MODIFY:
			final File modifiedFile = event.getModifiedFile();
			
			if (entityNode != null) {
				if (entity instanceof SearchEntry) {
					final SearchEntry entry = (SearchEntry) entity;
					
					if (modifiedFile.exists()) {
						addSearchEntryQuery(entry, modifiedFile, entityNode);
					} else {
						removeSearchEntryQuery(modifiedFile, entityNode);
					}
				} else if (entity instanceof Export) {
					final Export export = (Export) entity;
					
					if (modifiedFile.exists()) {
						addExportEntry(export, modifiedFile, entityNode);
					} else {
						removeExportEntry(modifiedFile, entityNode);
					}
					
				} else if (entity.getBaseFile().equals(modifiedFile)) {
					this.treeModel.replaceNode(
						entityNode, 
						RepositoryTreeModel.createSequenceEntityNode(entity)
					);
				}
			}
			break;
		}
	}
	
	private DefaultMutableTreeNode getUserObjectChildNode(
		final DefaultMutableTreeNode node, Object userObject
	) {
		for (int i = 0; i < node.getChildCount(); i++) {
			if (node.getChildAt(i) instanceof DefaultMutableTreeNode) {
				final DefaultMutableTreeNode child = 
					(DefaultMutableTreeNode) node.getChildAt(i);
				
				if (child.getUserObject().equals(userObject)) 
					return child;
			}
		}
		
		return null;
	}
	
	private SearchEntry.Query getQueryForFile(SearchEntry searchEntry, File queryFile) {
		for (SearchEntry.Query query : searchEntry.listQueries()) {
			if (query.getBaseFile().equals(queryFile)) {
				return query;
			}
		}
		return null;
	}

	private void addSearchEntryQuery(
		final SearchEntry searchEntry,
		final File modifiedFile, 
		final DefaultMutableTreeNode searchEntryNode
	) {
		final SearchEntry.Query query = this.getQueryForFile(searchEntry, modifiedFile);
		
		if (query != null && this.getUserObjectChildNode(searchEntryNode, query) == null) {
			this.treeModel.insertNode(
				searchEntryNode, 
				RepositoryTreeModel.createSearchEntryQuery(query)
			);
		}
	}

	private void removeSearchEntryQuery(
		final File modifiedFile,
		final DefaultMutableTreeNode searchEntryNode
	) {
		final DefaultMutableTreeNode queryNode = 
			this.getSubEntityNodeForFile(searchEntryNode, modifiedFile);
		
		if (queryNode != null) {
			this.treeModel.deleteNode(searchEntryNode, queryNode);
		}
	}
	
	private ExportEntry getExportEntryForFile(Export export, File file) {
		for (ExportEntry entry : export.listEntries()) {
			if (entry.getBaseFile().equals(file) ||
				entry.getOutFile().equals(file) ||
				Arrays.asList(entry.getSequenceFiles()).contains(file)
			) {
				return entry;
			}
		}
		
		return null;
	}
	
	private void addExportEntry(
		final Export export,
		final File modifiedFile, 
		final DefaultMutableTreeNode exportNode
	) {
		final ExportEntry entry = this.getExportEntryForFile(export, modifiedFile);
		
		if (entry != null) {
			if (entry.getBaseFile().equals(modifiedFile)) {
				if (this.getUserObjectChildNode(exportNode, entry) == null) {
					this.treeModel.insertNode(
						exportNode, 
						RepositoryTreeModel.createExportEntryNode(entry)
					);
				}
			} else {
				final DefaultMutableTreeNode entryNode = 
					this.getUserObjectChildNode(exportNode, entry);
				
				if (entryNode != null) {
					if (entry.getOutFile().equals(modifiedFile)) {
						if (this.getUserObjectChildNode(entryNode, entry.getOutFile().getName()) == null)
							throw new IllegalStateException("Inconsitent Export Entry state. Missing out file.");
					} else { // It's a .txt file
						if (this.getUserObjectChildNode(entryNode, modifiedFile.getName()) == null) {
							this.treeModel.insertNode(
								entryNode, 
								RepositoryTreeModel.createExportEntryTextFileNode(modifiedFile)
							);
						}
					}
				}
			}
		}
	}
	
	private DefaultMutableTreeNode getSubEntityNodeForFile(
		DefaultMutableTreeNode exportNode, File file
	) {
		for (int j = 0; j < exportNode.getChildCount(); j++) {
			if (exportNode.getChildAt(j) instanceof DefaultMutableTreeNode) {
				final DefaultMutableTreeNode entryChild = 
					(DefaultMutableTreeNode) exportNode.getChildAt(j);
				
				if (entryChild.getUserObject() instanceof SequenceEntity) {
					final File entryFile = ((SequenceEntity) entryChild.getUserObject()).getBaseFile();
					
					if (entryFile.equals(file)) {
						return entryChild;
					}
				}
			}
		}
		
		return null;
	}

	private void removeExportEntry(
		final File modifiedFile,
		final DefaultMutableTreeNode exportNode
	) {
		DefaultMutableTreeNode entryNode = 
			this.getSubEntityNodeForFile(exportNode, modifiedFile);
		
		if (entryNode != null) { // Entry deleted
			this.treeModel.deleteNode(exportNode, entryNode);
		} else {
			for (int i = 0; i < exportNode.getChildCount(); i++) {
				if (exportNode.getChildAt(i) instanceof DefaultMutableTreeNode) {
					entryNode = (DefaultMutableTreeNode) exportNode.getChildAt(i);
					
					if (entryNode.getUserObject() instanceof ExportEntry) {
						final ExportEntry entry = (ExportEntry) entryNode.getUserObject();
						
						if (entry.getOutFile().equals(modifiedFile)) {
							throw new IllegalStateException("Inconsitent Export Entry state. Out file can't be deleted.");
						} else {
							final DefaultMutableTreeNode fileNode = 
								this.getUserObjectChildNode(entryNode, modifiedFile.getName());
							
							if (fileNode != null) {
								this.treeModel.deleteNode(entryNode, fileNode);
								
								break;
							}
						}
					}
				}
			}
		}
	}
}