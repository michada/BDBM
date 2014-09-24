package es.uvigo.esei.sing.bdbm.gui.repository;

import javax.swing.tree.DefaultMutableTreeNode;

class TypedMutableTreeNode<T> extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
	
	public TypedMutableTreeNode() {
		super();
	}

	public TypedMutableTreeNode(T userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public TypedMutableTreeNode(T userObject) {
		super(userObject);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getUserObject() {
		return (T) super.getUserObject();
	}
}
