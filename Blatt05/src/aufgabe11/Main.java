package aufgabe11;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
	public static final int N_CONSUMER = 5;

	public static void main(String[] args) {
		Path startPath = Paths.get("/home/bt90");
		PathMatcher filter = FileSystems.getDefault().getPathMatcher("glob:*.dat");

		BlockingQueue<PathWrapper> queue = new LinkedBlockingQueue<>();

		ExecutorService producerExec = Executors.newSingleThreadExecutor();
		ExecutorService consumerExec = Executors.newFixedThreadPool(N_CONSUMER);

		producerExec.execute(new Producer(startPath, queue));
		for (int i = 0; i < N_CONSUMER; i++) {
			consumerExec.execute(new Consumer(queue, filter));
		}
		
		producerExec.shutdown();
		consumerExec.shutdown();
	}
}
