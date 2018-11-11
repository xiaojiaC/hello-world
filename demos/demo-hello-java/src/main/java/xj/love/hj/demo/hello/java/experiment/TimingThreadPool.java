package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计任务执行时间的线程池执行者。
 *
 * @author xiaojia
 * @since 1.0
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    private final Logger logger = LoggerFactory.getLogger(TimingThreadPool.class);
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    private TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 线程执行任务前前调用。如果beforeExecute中抛出一个RuntimeException, 那么任务不会执行，且afterExecute也不会被调用。
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        startTime.set(System.nanoTime());
        logger.info(String.format("Start: at time=%s", startTime.get()));

        // throw new RuntimeException();
    }

    /**
     * 线程完执行任务后调用。无论从run()中正常返回还是抛出异常返回，afterExecute都会被调用，除非任务完成后抛出一个Error。
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long costTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(costTime);
            logger.info(String.format("End: cost time=%dns", costTime));
        } catch (Exception e) {
            super.afterExecute(r, t);
        }
    }

    /**
     * 线程池完成关闭操作时调用。
     */
    @Override
    protected void terminated() {
        try {
            logger.info(
                    String.format("Terminated: avg time=%dns", totalTime.get() / numTasks.get()));
        } catch (Exception e) {
            super.terminated();
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Runnable> blockingQueue = new java.util.concurrent.ArrayBlockingQueue<>(3);
        ThreadPoolExecutor executor = new TimingThreadPool(2, 2, 0L, TimeUnit.SECONDS,
                blockingQueue);
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                System.out.println("Executing ......");
            });
        }
        /**
         * shutdown: 执行平滑关闭，不再接受新的任务，同时等待已经提交的任务执行完成--包含那些还未开始执行的任务。
         * shutdownNow: 粗暴关闭，尝试取消所有在运行中的任务，且不再启动队列中尚未开始执行的任务。
         */
        executor.shutdown();
    }
}
