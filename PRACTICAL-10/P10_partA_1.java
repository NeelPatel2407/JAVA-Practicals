import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class P10_partA_1 {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for(int i = 1; i <= 10; i++) {
            final int taskId = i;

            pool.submit(() -> {
                String threadName = Thread.currentThread().getName();

                System.out.println("Task " + taskId + " started by " + threadName);

                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Task " + taskId + " finished by " + threadName);
            });
        }

        pool.shutdown();

        try {
            if(pool.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("All tasks completed.");
            } else {
                System.out.println("Some tasks are still running.");
                pool.shutdownNow();
            }
        } catch(InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}