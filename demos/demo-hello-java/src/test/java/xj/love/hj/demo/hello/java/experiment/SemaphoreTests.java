package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore: 信号量。用来控制同时访问特定资源的线程数量，它通过协调各个线程，以保证合理的使用公共资源。
 *
 * - 可以用于做流量控制，特别是公用资源有限的应用场景。
 */
public class SemaphoreTests {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(6);

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);

        for (int i = 0; i < 4; i++) {
            EXECUTOR.execute(() -> {
                try {
                    semaphore.acquire();

                    System.out.println(Thread.currentThread().getName() + "获取令牌成功，执行业务中...");
                    Thread.sleep(100);

                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        EXECUTOR.shutdown();
    }
}
