package es.uvigo.esei.sing.bdbm.persistence.watcher;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


class PollingRepositoryWatchersPool {
	private static volatile PollingRepositoryWatchersPool instance;
	
	public static PollingRepositoryWatchersPool getInstance() {
		if (instance == null) {
			synchronized(PollingRepositoryWatchersPool.class) {
				if (instance == null) {
					instance = new PollingRepositoryWatchersPool();
					instance.startPolling();
				}
			}
		}
		
		return instance;
	}

	private final ScheduledExecutorService executor;
	private final ReadWriteLock lock;
	private final Set<PollingRepositoryWatcher> watchers;
	private final PollingRepositoryWatchersTask watcherTask;
	
	private PollingRepositoryWatchersPool() {
		this.lock = new ReentrantReadWriteLock();
		
		this.executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			private final AtomicInteger i = new AtomicInteger(0);
			
			@Override
			public Thread newThread(Runnable r) {
				final String name = new StringBuilder()
					.append(r.getClass())
					.append("-")
					.append(i.getAndIncrement())
				.toString();
				
				return new Thread(r, name);
			}
		});
		
		this.watchers = new HashSet<PollingRepositoryWatcher>();
		this.watcherTask = new PollingRepositoryWatchersTask();
	}
	
	private void startPolling() {
		this.executor.scheduleWithFixedDelay(this.watcherTask, 0L, 1L, TimeUnit.SECONDS);
	}
	
	private final class PollingRepositoryWatchersTask implements Runnable {
		@Override
		public void run() {
			PollingRepositoryWatchersPool.this.getWriteLock().lock();
			try {
				final Map<File, List<PollingRepositoryWatcher>> watchedFiles = 
					PollingRepositoryWatchersPool.this.getWatchedFiles();
				
				for (Map.Entry<File, List<PollingRepositoryWatcher>> entry : watchedFiles.entrySet()) {
					final File file = entry.getKey();
					
					if (!file.exists()) {
						fireFileDeleted(file, entry.getValue());
					} else if (file.isDirectory()) {
						checkAndFireFileCreated(file, entry.getValue());
					}
				}
			} finally {
				PollingRepositoryWatchersPool.this.getWriteLock().unlock();
			}
		}
	}

	private static void checkAndFireFileCreated(File file, List<PollingRepositoryWatcher> fileWatchers) {
		final Stack<File> subFiles = new Stack<File>();
		subFiles.addAll(Arrays.asList(file.listFiles()));
		
		while (!subFiles.isEmpty()) {
			final File subFile = subFiles.pop();
			
			for (PollingRepositoryWatcher watcher : fileWatchers) {
				if (!watcher.isRegistered(subFile)) {
					watcher.fileCreated(subFile);
					
					if (subFile.isDirectory()) {
						subFiles.addAll(Arrays.asList(subFile.listFiles()));
					}
				}
			}
		}
	}
	
	private static void fireFileDeleted(File file, List<PollingRepositoryWatcher> watchers) {
		for (PollingRepositoryWatcher watcher : watchers) {
			watcher.fileDeleted(file);
		}
	}
	
	public void addWatcher(PollingRepositoryWatcher watcher) {
		if (!this.watchers.contains(watcher))
			this.watchers.add(watcher);
	}
	
	public void removeWatcher(PollingRepositoryWatcher watcher) {
		this.watchers.remove(watcher);
	}

	private Map<File, List<PollingRepositoryWatcher>> getWatchedFiles() {
		final Map<File, List<PollingRepositoryWatcher>> watchers = 
			new HashMap<File, List<PollingRepositoryWatcher>>();
		
		for (PollingRepositoryWatcher watcher : this.watchers) {
			for (File file : watcher.listRegisteredFiles()) {
				if (!watchers.containsKey(file)) {
					watchers.put(file, new LinkedList<PollingRepositoryWatcher>());
				}
				watchers.get(file).add(watcher);
			}
		}
		
		return watchers;
	}
	
	public Lock getReadLock() {
		return this.lock.readLock();
	}
	
	public Lock getWriteLock() {
		return this.lock.writeLock();
	}
}
