package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * CyclicBarrier: 可循环使用的屏障。让一组线程到达一个屏障(也可以叫同步点)时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续运行。
 *
 * CountDownLatch的计数器只能使用一次,而CyclicBarrier的计数器可以使用reset()方法重置。所以CyclicBarrier能
 * 处理更为复杂的业务场景。例如,如果计算发生错误,可以重置计数器,并让线程重新执行一次。
 */
public class CyclicBarrierTests {

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDataValidation() throws Exception {
        ConcurrentHashMap<String, Long> result = new ConcurrentHashMap<>();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, () -> {
            System.out.println("3个线程均计算完毕了");
        });

        for (int i = 1; i <= 3; i++) { // ３个任务线程
            final int j = i;
            Future future = executor.submit(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result.put(Thread.currentThread().getName(), j * 100L);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    System.out.println("线程在等待过程中被中断");
                } catch (BrokenBarrierException e) {
                    System.out.println("如果另一个线程在当前线程等待时被中断或超时，或者屏障被重置，或者在调"
                            + "用await时屏障被破坏，或屏障操作（如果存在）由于异常而失败");
                }
            });
            future.isDone();
        }

        cyclicBarrier.await(); // 主线程归总，等待３个任务线程执行完毕后归总结果
        assertEquals(600L, result.values().stream().mapToLong(Long::longValue).sum());
    }

    @Test
    public void testBarrierBroken() throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (Exception ignored) {
                }
            }
        });
        thread.start();
        thread.interrupt();

        expectedException.expect(BrokenBarrierException.class);
        cyclicBarrier.await();
        assertTrue(cyclicBarrier.isBroken());
    }
}
