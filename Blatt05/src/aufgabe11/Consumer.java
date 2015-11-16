package aufgabe11;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
	private final BlockingQueue<Path> queue;
	private final PathMatcher filter;
	
	public Consumer(BlockingQueue<Path> queue, PathMatcher filter) {
		this.queue = queue;
		this.filter = filter;
	}

	@Override
	public void run() {
		try {
			Path file = queue.take();
			
			if (filter.matches(file)) {
				System.out.println(file);
			}
			// TODO signal producer
		} catch (InterruptedException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
	}

}
