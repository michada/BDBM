package es.uvigo.esei.sing.bdbm.gui.repository;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import es.uvigo.esei.sing.bdbm.gui.repository.RepositoryTreeModel.TextFileMutableTreeNode;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

public class RepositoryTreeRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	
	final static ImageIcon ICON_FASTA = 
		new ImageIcon(RepositoryTreeRenderer.class.getResource("images/fasta.png"));
	final static ImageIcon ICON_REGULAR_DATABASE = 
		new ImageIcon(RepositoryTreeRenderer.class.getResource("images/database.png"));
	final static ImageIcon ICON_AGGREGATED_DATABASE = 
		new ImageIcon(RepositoryTreeRenderer.class.getResource("images/agg-database.png"));
	final static ImageIcon ICON_SEARCH_ENTRY = 
		new ImageIcon(RepositoryTreeRenderer.class.getResource("images/search-entry.png"));
	final static ImageIcon ICON_EXPORT = 
		new ImageIcon(RepositoryTreeRenderer.class.getResource("images/export.png"));
	final static ImageIcon ICON_EXPORT_OUTPUT = 
			new ImageIcon(RepositoryTreeRenderer.class.getResource("images/output.png"));
	final static ImageIcon ICON_SEQUENCE = 
			new ImageIcon(RepositoryTreeRenderer.class.getResource("images/sequence.png"));

	@Override
	public Component getTreeCellRendererComponent(
		JTree tree, Object value,
		boolean sel, boolean expanded, boolean leaf, int row,
		boolean hasFocus
	) {
		if (value instanceof TextFileMutableTreeNode) {
			final TextFileMutableTreeNode<?> node = (TextFileMutableTreeNode<?>) value;
			
			this.setLeafIcon(node.getIcon());
			this.setOpenIcon(node.getIcon());
			this.setClosedIcon(node.getIcon());
		} else if (value instanceof DefaultMutableTreeNode) {
			final Object nodeValue = ((DefaultMutableTreeNode) value).getUserObject();
			
			if (nodeValue instanceof Fasta) {
				this.setLeafIcon(ICON_FASTA);
			} else if (nodeValue instanceof Database) {
				if (((Database) nodeValue).isAggregated()) {
					this.setLeafIcon(ICON_AGGREGATED_DATABASE);
				} else {
					this.setLeafIcon(ICON_REGULAR_DATABASE);
				}
			} else if (nodeValue instanceof SearchEntry) {
				this.setOpenIcon(ICON_SEARCH_ENTRY);
				this.setClosedIcon(ICON_SEARCH_ENTRY);
			} else if (nodeValue instanceof Export) {
				this.setOpenIcon(ICON_EXPORT);
				this.setClosedIcon(ICON_EXPORT);
			} else {
				this.setLeafIcon(this.getDefaultLeafIcon());
				this.setOpenIcon(this.getDefaultOpenIcon());
				this.setClosedIcon(this.getDefaultOpenIcon());
			}
		} else {
			this.setLeafIcon(this.getDefaultLeafIcon());
			this.setOpenIcon(this.getDefaultOpenIcon());
			this.setClosedIcon(this.getDefaultOpenIcon());
		}
		
		return super.getTreeCellRendererComponent(
			tree, value, sel, expanded, leaf, row, hasFocus
		);
	}
}
