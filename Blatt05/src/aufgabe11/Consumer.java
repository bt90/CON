package aufgabe11;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
	private final BlockingQueue<PathWrapper> queue;
	private final PathMatcher filter;

	public Consumer(BlockingQueue<PathWrapper> queue, PathMatcher filter) {
		this.queue = queue;
		this.filter = filter;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				PathWrapper wrapper = queue.take();
				Path file = wrapper.getPath();
				
				// producer finished
				if (file == null) {
					// inform other consumers
					queue.put(wrapper);
					return;
				}
				
				if (filter.matches(file.getFileName())) {
					System.out.println(file);
				}
			} catch (InterruptedException e) {
				// ensure to reset the interrupt flag
				Thread.currentThread().interrupt();
			}
		}
	}
}
