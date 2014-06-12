package es.uvigo.esei.sing.bdbm.persistence.watcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryWatcherEvent.Type;

public abstract class AbstractRepositoryWatcher implements RepositoryWatcher {
	protected final List<RepositoryWatcherListener> listeners;
	
	public AbstractRepositoryWatcher() {
		this.listeners = Collections.synchronizedList(
			new LinkedList<RepositoryWatcherListener>()
		);
	}
	
	@Override
	public void addRepositoryWatcherListener(RepositoryWatcherListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public boolean removeRepositoryWatcherListener(RepositoryWatcherListener listener) {
		return this.listeners.remove(listener);
	}

	@Override
	public boolean containsRepositoryWatcherListener(RepositoryWatcherListener listener) {
		return this.listeners.contains(listener);
	}

	protected void fireFileCreated(File file) {
		fireEvent(file, RepositoryWatcherEvent.Type.CREATE);
	}

	protected void fireFileDeleted(File file) {
		fireEvent(file, RepositoryWatcherEvent.Type.DELETE);
	}

	protected void fireEvent(File file, final Type type) {
		final List<RepositoryWatcherListener> listeners = 
			new ArrayList<RepositoryWatcherListener>(this.listeners);
		
		for (RepositoryWatcherListener listener : listeners) {
			listener.repositoryChanged(new RepositoryWatcherEvent(file, type));
		}
	}
}