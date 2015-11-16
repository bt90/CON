package aufgabe11;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

public class Main {
	public static final int N_CONSUMER = 5;

	public static void main(String[] args) {
		Path startPath = Paths.get("/home/bt90");
		PathMatcher filter = FileSystems.getDefault().getPathMatcher("glob:*.png");
		
		BlockingQueue<Path> queue = new LinkedBlockingQueue<>();
		
		ExecutorService producerExec  = Executors.newSingleThreadExecutor();
		ExecutorService consumerExec = Executors.newFixedThreadPool(N_CONSUMER);
		
		producerExec.execute(new Producer(startPath, queue, consumerExec));
		try {
			while (true) {
				consumerExec.execute(new Consumer(queue, filter));
			}
		} catch (RejectedExecutionException e) {
			System.out.println("Consumers killed by Producer");
		}
	}
}
