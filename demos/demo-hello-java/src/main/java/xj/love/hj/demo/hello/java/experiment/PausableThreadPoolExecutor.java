package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可暂停的线程池执行者。
 *
 * @author xiaojia
 * @since 1.0
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean isPaused = false;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();

    private PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) {
                unpaused.await();
            }
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        BlockingQueue<Runnable> blockingQueue = new java.util.concurrent.ArrayBlockingQueue<>(10);
        PausableThreadPoolExecutor executor = new PausableThreadPoolExecutor(1, 1, 0L,
                TimeUnit.SECONDS,
                blockingQueue);
        executor.pause();
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                System.out.println("Executing ......");
            });
        }
        Thread.sleep(1000);
        executor.resume();
        executor.shutdown();
    }
}
