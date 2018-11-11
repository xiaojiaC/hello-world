package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * CountDownLatch: 闭锁。允许一个或多个线程等待其他线程完成操作。
 * <pre>
 * - 计数器必须大于等于0,只是等于0时候,计数器就是0,调用await方法时不会阻塞当前线程。
 * - CountDownLatch不可以重新初始化或者修改CountDownLatch对象的内部计数器的值。
 * - 一个线程调用countDown方法happen-before另外一个线程调用await方法。
 * </pre>
 */
public class CountDownLatchTests {

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @Test
    public void testAwait() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        mockDataParseTasks(countDownLatch);

        countDownLatch.await();
    }

    @Test
    public void testAwaitTimeout() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        mockDataParseTasks(countDownLatch);

        boolean allFinished = countDownLatch.await(100, TimeUnit.MILLISECONDS);
        assertFalse(allFinished);
    }

    private void mockDataParseTasks(CountDownLatch countDownLatch) {
        for (int i = 0; i < 3; i++) {
            Future future = executor.submit(() -> {
                try {
                    Thread.sleep(200); // 模拟数据解析工作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("I'm finished.");
                countDownLatch.countDown();
            });
            future.isDone();
        }
    }
}
