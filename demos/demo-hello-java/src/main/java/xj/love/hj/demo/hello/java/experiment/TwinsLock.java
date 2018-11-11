package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 共享式访问，支持同一时刻2个线程同时访问共享资源。
 *
 * @author xiaojia
 * @since 1.0
 */
public class TwinsLock implements Lock {

    private static final Sync SYNC = new Sync(2);

    @Override
    public void lock() {
        SYNC.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        SYNC.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return SYNC.tryAcquireShared(1) >= 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return SYNC.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        SYNC.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * AQS同步器
     */
    private static final class Sync extends AbstractQueuedSynchronizer {

        Sync(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("count must large than zero.");
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            while (true) {
                int current = getState();
                int newCount = current - arg;
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            while (true) {
                int current = getState();
                int newCount = current + arg;
                if (compareAndSetState(current, newCount)) {
                    return true;
                }
            }
        }
    }
}
