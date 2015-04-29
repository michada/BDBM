package es.uvigo.esei.sing.bdbm.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public final class DirectoryUtils {
	private DirectoryUtils() {}
	
	public static void deleteIfExists(File directory) throws IOException {
		deleteIfExists(directory.toPath());
	}
	
	public static void deleteIfExists(Path directory) throws IOException {
		if (Files.exists(directory)) {
			if (!Files.isDirectory(directory)) {
				throw new IllegalArgumentException("input path must be a directory");
			} else {
				Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file,	BasicFileAttributes attrs) 
					throws IOException {
						Files.delete(file);
						
						return FileVisitResult.CONTINUE;
					}
					
					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
						Files.delete(dir);
						
						return FileVisitResult.CONTINUE;
					}
				});
			}
		}
	}
}
