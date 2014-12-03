/**
 * 
 */
package es.uvigo.esei.sing.bdbm.utils;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.MainWindow;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.tree.AIBenchTreeMouseListener;

public class ClipboardViewsMouseListener extends AIBenchTreeMouseListener {
	public final static ClipboardViewsMouseListener setAsDefaultClipboardTreeListener() {
		final JTree tree = Workbench.getInstance().getTreeManager().getAIBenchClipboardTree();
		
		final MouseListener[] listeners = tree.getMouseListeners();  
		for (MouseListener listener:listeners) {
			if (listener instanceof AIBenchTreeMouseListener) {
				tree.removeMouseListener(listener);
			}
		}
		
		final ClipboardViewsMouseListener listener = new ClipboardViewsMouseListener();
		tree.addMouseListener(listener);
		
		return listener;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && !e.isPopupTrigger()) {
			if (e.getComponent() instanceof JTree) {
				final JTree tree = (JTree) e.getComponent();
				
				final TreePath selectionPath = tree.getPathForLocation(e.getX(), e.getY());
				
				if (selectionPath != null) {
					final int pathCount = selectionPath.getPathCount();
					
					// First ClassTreeNode child.
					if (pathCount <= 2) {
						super.mouseClicked(e);
					} else if (pathCount > 2) {
						if (selectionPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
							final DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
							if (targetNode.getUserObject() != null) {
								final Object targetItem = (targetNode.getUserObject() instanceof ClipboardItem)?
									((ClipboardItem) targetNode.getUserObject()).getUserData():
									targetNode.getUserObject();
								
								
								final Object[] path = selectionPath.getPath();
								final MainWindow mainWindow = (MainWindow) Workbench.getInstance().getMainFrame();
								
								boolean showed = false;
								for (int i = 2; i < (pathCount - 1) && !showed; i++) {
									if (path[i] instanceof DefaultMutableTreeNode) {
										final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path[i];
										
										if (node.getUserObject() instanceof ClipboardItem) {
											final ClipboardItem item = (ClipboardItem) node.getUserObject();
											
											for (Component view : mainWindow.getDataViews((ClipboardItem) item)) {
												if (view instanceof ClipboardItemView) {
													final ClipboardItemView ciView = (ClipboardItemView) view;
													if (ciView.showClipboardItem(targetItem)) {
														Workbench.getInstance().showData(item);
														
														showed = true;
														break;
													}
												}
											}
										}
									}
								}
								
								if (!showed) {
									super.mouseClicked(e);
								}
							}
						}
					}
				}
			}
		} else {
			super.mouseClicked(e);
		}
	}
}