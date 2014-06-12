package es.uvigo.esei.sing.bdbm.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.ORF;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

class RepositoryTreeRenderer extends DefaultTreeCellRenderer {
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
	final static ImageIcon ICON_ORF = 
		new ImageIcon(RepositoryTreeRenderer.class.getResource("images/orf.png"));

	@Override
	public Component getTreeCellRendererComponent(
		JTree tree, Object value,
		boolean sel, boolean expanded, boolean leaf, int row,
		boolean hasFocus
	) {
		if (value instanceof DefaultMutableTreeNode) {
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
			} else if (nodeValue instanceof ORF) {
				this.setLeafIcon(ICON_ORF);
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
