package es.uvigo.esei.sing.bdbm.persistence.watcher;

import java.util.EventListener;

public interface RepositoryListener extends EventListener {
	public void repositoryChanged(RepositoryEvent event);
}
