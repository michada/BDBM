package es.uvigo.esei.sing.bdbm.gui.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

class SortedMutableTreeNode<T, C> extends TypedMutableTreeNode<T> {
	private static final long serialVersionUID = 1L;

	private final Comparator<C> comparator;
	
	public SortedMutableTreeNode(Comparator<C> comparator) {
		super();
		this.comparator = comparator;
	}

	public SortedMutableTreeNode(Comparator<C> comparator, T userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		this.comparator = comparator;
	}

	public SortedMutableTreeNode(Comparator<C> comparator, T userObject) {
		super(userObject);
		this.comparator = comparator;
	}
	
	@SuppressWarnings("unchecked")
	public List<TypedMutableTreeNode<C>> getChildNodes() {
		final List<TypedMutableTreeNode<C>> childrenList = new ArrayList<>();
		
		if (this.children != null)
			childrenList.addAll(this.children);

		return childrenList;
	}

	public void add(C userObject) {
		this.add(new TypedMutableTreeNode<>(userObject));
	}
	
	public int add(TypedMutableTreeNode<C> newChild) {
		if (!allowsChildren) {
			throw new IllegalStateException("node does not allow children");
		} else if (newChild == null) {
			throw new IllegalArgumentException("new child is null");
		} else if (isNodeAncestor(newChild)) {
			throw new IllegalArgumentException("new child is an ancestor");
		} else if (alreadyContainsChild(newChild)) {
			//TODO: This should not be needed, but Export nodes were being duplicated when inserted.
			throw new IllegalArgumentException("new child is already child of this node");
		}

		final MutableTreeNode oldParent = (MutableTreeNode) newChild.getParent();

		if (oldParent != null) {
			oldParent.remove(newChild);
		}
		newChild.setParent(this);
		if (this.children == null) {
			this.children = new Vector<TypedMutableTreeNode<C>>(
				Collections.singleton(newChild)
			);
			
			return 0;
		} else {
			@SuppressWarnings("unchecked")
			final Vector<TypedMutableTreeNode<C>> typedChildren = this.children;
			typedChildren.add(newChild);
			
			Collections.sort(typedChildren, new Comparator<TypedMutableTreeNode<C>>() {
				@Override
				public int compare(TypedMutableTreeNode<C> o1, TypedMutableTreeNode<C> o2) {
					return comparator.compare((C) o1.getUserObject(), (C) o2.getUserObject());
				}
			});
			
			return typedChildren.indexOf(newChild);
		}
	}
	
	private boolean alreadyContainsChild(TypedMutableTreeNode<C> newChild) {
		if (this.children == null) {
			return false;
		} else {
			@SuppressWarnings("unchecked")
			final Vector<TypedMutableTreeNode<C>> typedChildren = this.children;
			
			for (TypedMutableTreeNode<C> child : typedChildren) {
				if (child.getUserObject().equals(newChild.getUserObject())) {
					return true;
				}
			}
			
			return false;
		}
	}

	@Override
	public void insert(MutableTreeNode newChild, int childIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(MutableTreeNode newChild) {
		throw new UnsupportedOperationException();
	}
}
