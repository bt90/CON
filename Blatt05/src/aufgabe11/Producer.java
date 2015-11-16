package aufgabe11;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class Producer implements Runnable {
	private final Path start;
	private final BlockingQueue<Path> queue;
	private final ExecutorService consumerExec;
	
	public Producer(Path start, BlockingQueue<Path> queue, ExecutorService consumerExec) {
		this.start = start;
		this.queue = queue;
		this.consumerExec = consumerExec;
	}

	@Override
	public void run() {
		FileVisitor<Path> visitor = new ProducingVisitor();

		try {
			Files.walkFileTree(start, visitor);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			consumerExec.shutdown();
		}
	}

	private class ProducingVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			try {
				queue.put(file);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// TODO check if consumers are satisfied and return FileVisitResult.TERMINATE
			
			return FileVisitResult.CONTINUE;
		}
	}
}
