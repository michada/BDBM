package es.uvigo.esei.sing.bdbm.gui;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public abstract class FileChangeWatcherRunnable implements Runnable {
	protected final Path file;
	
	public FileChangeWatcherRunnable(File file) 
	throws IllegalArgumentException {
		this(file.toPath());
	}
	
	public FileChangeWatcherRunnable(Path file) 
	throws IllegalArgumentException {
		if (!file.toFile().isFile())
			throw new IllegalArgumentException("Watched file must be a regular file");
		
		this.file = file;
	}
	
	public static Thread watchFile(final File file, final Runnable changeCallback) {
		return new Thread(new FileChangeWatcherRunnable(file) {
			@Override
			protected void fileChanged() {
				changeCallback.run();
			}
		});
	}
	
	protected abstract void fileChanged();

	@Override
	public void run() {
		try {
			final WatchService watchService = FileSystems.getDefault().newWatchService();
			this.file.getParent().register(
				watchService, StandardWatchEventKinds.ENTRY_MODIFY
			);
			
			while (true) {
			    final WatchKey wk = watchService.take();
			    for (WatchEvent<?> event : wk.pollEvents()) {
			        //we only register "ENTRY_MODIFY" so the context is always a Path.
			        final Path changed = (Path) event.context();
			        
			        if (changed.getFileName().equals(this.file.getFileName())) {
			        	this.fileChanged();
			        }
			    }
			    
			    wk.reset();
			}
		} catch (Exception e) {}
	}
}
