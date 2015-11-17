package aufgabe11;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
	private final Path start;
	private final BlockingQueue<PathWrapper> queue;
	
	public Producer(Path start, BlockingQueue<PathWrapper> queue) {
		this.start = start;
		this.queue = queue;
	}

	@Override
	public void run() {
		FileVisitor<Path> visitor = new ProducingVisitor();

		try {
			Files.walkFileTree(start, visitor);
			
			// signal consumers
			queue.put(new PathWrapper(null));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class ProducingVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			try {
				queue.put(new PathWrapper(file));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// TODO check if consumers are satisfied and return FileVisitResult.TERMINATE
			
			return FileVisitResult.CONTINUE;
		}
	}
}
