package es.uvigo.esei.sing.bdbm.persistence.watcher;

import java.io.File;

public class RepositoryWatcherEvent {
	public enum Type {
		CREATE,
		DELETE
	};
	
	private final File file;
	private final Type type;
	public RepositoryWatcherEvent(File file, Type type) {
		this.file = file;
		this.type = type;
	}
	
	public File getFile() {
		return file;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(this.getType().name())
			.append(": ").append(this.getFile())
		.toString();
	}
}
