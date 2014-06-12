package es.uvigo.esei.sing.bdbm.persistence.watcher;

import java.util.EventListener;

public interface RepositoryWatcherListener extends EventListener {
	public void repositoryChanged(RepositoryWatcherEvent event);
}
