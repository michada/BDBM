package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.watcher.PollingRepositoryWatcher;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryWatcher;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryWatcherEvent;
import es.uvigo.esei.sing.bdbm.persistence.watcher.RepositoryWatcherListener;

public abstract class AbstractSearchEntry extends AbstractSequenceEntity implements SearchEntry {
	private static final boolean DEFAULT_WATCH = true;

	public static SearchEntry newSearchEntry(SequenceType sequenceType, File directory) throws IOException {
		return newSearchEntry(sequenceType, directory, DEFAULT_WATCH);
	}
	
	public static SearchEntry newSearchEntry(SequenceType sequenceType, File directory, boolean watch) throws IOException {
		switch (sequenceType) {
		case PROTEIN:
			return new DefaultProteinSearchEntry(directory, watch);
		case NUCLEOTIDE:
			return new DefaultNucleotideSearchEntry(directory, watch);
		default:
			throw new IllegalStateException("Unknown sequence type");
		}
	}
	
//	protected final WatcherService watcher;
	protected final RepositoryWatcher watcher;
	
	protected AbstractSearchEntry(SequenceType type, File file) throws IOException {
		this(type, file, DEFAULT_WATCH);
	}
	
	protected AbstractSearchEntry(SequenceType type, File file, boolean watch) throws IOException {
		super(type, file);
		
		this.watcher = new PollingRepositoryWatcher();
		this.watcher.addRepositoryWatcherListener(new SearchEntryRepositoryListener());
		
		if (watch)
			this.watcher.register(this.getBaseFile());
		
//		this.watcher = new WatcherService(file.toPath());
//		this.watcher.start();
	}
	
	private final class SearchEntryRepositoryListener implements RepositoryWatcherListener {
		@Override
		public void repositoryChanged(RepositoryWatcherEvent event) {
			switch (event.getType()) {
			case DELETE:
				if (event.getFile().equals(AbstractSearchEntry.this.getBaseFile())) {
					AbstractSearchEntry.this.watcher.clear();
					AbstractSearchEntry.this.watcher.removeRepositoryWatcherListener(this);
				}
				// No break
			case CREATE:
				AbstractSearchEntry.this.setChanged();
				AbstractSearchEntry.this.notifyObservers(event.getFile());
				break;
			}
		}
	}
	
//	private final static class SearchEntryRepositoryListener implements RepositoryListener {
//		private final WeakReference<AbstractSearchEntry> searchEntry;
//		private final RepositoryWatcher watcher;
//		
//		public SearchEntryRepositoryListener(AbstractSearchEntry searchEntry, RepositoryWatcher watcher) {
//			this.searchEntry = new WeakReference<AbstractSearchEntry>(searchEntry);
//			this.watcher = watcher;
//		}
//		
//		@Override
//		public void repositoryChanged(RepositoryEvent event) {
//			final AbstractSearchEntry entry;
//			if (this.searchEntry.isEnqueued() || (entry = this.searchEntry.get()) == null) {
//				
//			} else {
//				
//			}
//		}
//	}
	
//	@Override
//	public List<Query> listQueries() {
//		final File[] files = this.getDirectory().listFiles((FileFilter) FileFileFilter.FILE);
//		final List<Query> queries = new ArrayList<SearchEntry.Query>(files.length);
//		
//		for (File file : files) {
//			queries.add(new DefaultQuery(file));
//		}
//		
//		return queries;
//	}
	
	public Query getQuery(String name) {
		for (Query query : this.listQueries()) {
			if (query.getName().equals(name)) {
				return query;
			}
		}
		
		return null;
	}
	
	@Override
	public File getDirectory() {
		return this.getBaseFile();
	}
	
	@Override
	public int compareTo(SearchEntry o) {
		return this.getDirectory().compareTo(o.getDirectory());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getDirectory() == null) ? 0 : this.getDirectory().hashCode());
		result = prime * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SearchEntry))
			return false;
		
		SearchEntry other = (SearchEntry) obj;
		if (this.getDirectory() == null) {
			if (other.getDirectory()!= null)
				return false;
		} else if (!this.getDirectory().equals(other.getDirectory()))
			return false;
		if (this.getType() != other.getType())
			return false;
		return true;
	}
//	
//	@Override
//	public String toString() {
//		return "SEARCH ENTRY: " + this.getName();
//	}
	
	public class DefaultQuery implements Query {
		private final File file;
		
		protected DefaultQuery(File file) {
			this.file = file;
		}

		@Override
		public SequenceType getType() {
			return AbstractSearchEntry.this.getType();
		}

		@Override
		public File getBaseFile() {
			return this.file;
		}

		@Override
		public String getName() {
			return this.file.getName();
		}
		
		@Override
		public String toString() {
			return this.file.getName();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.getBaseFile() == null) ? 0 : this.getBaseFile().hashCode());
			result = prime * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Query))
				return false;
			
			Query other = (Query) obj;
			if (this.getBaseFile() == null) {
				if (other.getBaseFile() != null)
					return false;
			} else if (!this.getBaseFile().equals(other.getBaseFile()))
				return false;
			if (this.getType() != other.getType())
				return false;
			return true;
		}
	}
	
//	@Override
//	protected void finalize() throws Throwable {
//		this.watcher.interrupt();
//	}
	
//	private class WatcherService extends WatchServiceThread {
////		private final WatchService watcher;
////		private final Thread shutdownHook;
//
//		public WatcherService(Path path) throws IOException {
//			super();
////			this.setDaemon(true);
//			
////			this.watcher = FileSystems.getDefault().newWatchService();
//			path.register(this.watcher, 
//				StandardWatchEventKinds.ENTRY_CREATE,
//				StandardWatchEventKinds.ENTRY_DELETE,
//				StandardWatchEventKinds.ENTRY_MODIFY
//			);
//			
////			this.shutdownHook = new Thread() {
////				@Override
////				public void run() {
////					try {
////						watcher.close();
////					} catch (IOException e) {
////						e.printStackTrace();
////					}
////				}
////			};
////			Runtime.getRuntime().addShutdownHook(this.shutdownHook);
//		}
//		
////		@Override
////		public void run() {
////			try {
////				for (;;) {
////					final WatchKey key;
////					try {
////						key = this.watcher.take();
////						
////						key.pollEvents();
////						AbstractSearchEntry.this.setChanged();
////						AbstractSearchEntry.this.notifyObservers();
////						key.reset();
////					} catch (InterruptedException ie) {
////						return;
////					}
////				}
////			} finally {
////				try {
////					this.watcher.close();
////				} catch (IOException e) {
////					e.printStackTrace();
////				} finally {
////					Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
////				}
////			}
////		}
//
//		@Override
//		protected boolean processKey(WatchKey key) {
//			key.pollEvents();
//			AbstractSearchEntry.this.setChanged();
//			AbstractSearchEntry.this.notifyObservers();
//			return key.reset();
//		}
//	}
}
