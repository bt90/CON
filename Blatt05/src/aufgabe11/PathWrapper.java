package aufgabe11;

import java.nio.file.Path;

public class PathWrapper {
	private final Path path;
	
	public PathWrapper(Path path) {
		this.path = path;
	}
	
	public Path getPath() {
		return path;
	}
}
