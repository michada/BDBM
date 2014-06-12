package es.uvigo.esei.sing.bdbm.persistence.watcher;

import java.io.File;
import java.util.List;

public interface RepositoryWatcher {
	public abstract void addRepositoryWatcherListener(RepositoryWatcherListener listener);
	public abstract boolean removeRepositoryWatcherListener(RepositoryWatcherListener listener);
	public abstract boolean containsRepositoryWatcherListener(RepositoryWatcherListener listener);
	
	public void register(File file);
	public void unregister(File file);
	public List<File> listRegisteredFiles();
	public boolean isRegistered(File file);
	public void clear();
}
