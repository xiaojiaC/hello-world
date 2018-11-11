package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用数组存放元素，实现可阻塞的存取队列。
 *
 * @author xiaojia
 * @since 1.0
 */
public class ArrayBlockingQueue {

    final Object[] items;
    int putIndex, takeIndex, count;

    ReentrantLock lock;
    Condition notFull, notEmpty;

    public ArrayBlockingQueue(int length) {
        items = new Object[length];
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    private void enqueue(Object object) {
        final Object[] items = this.items;
        items[putIndex] = object;
        if (++putIndex == items.length) {
            putIndex = 0;
        }
        count++;
        notEmpty.signal();
    }

    private Object dequeue() {
        final Object[] items = this.items;
        Object object = items[takeIndex];
        items[takeIndex] = null;
        if (++takeIndex == items.length) {
            takeIndex = 0;
        }
        count--;
        notFull.signal();
        return object;
    }

    public boolean offer(Object object) {
        if (object == null) {
            throw new NullPointerException("put object cannot is null");
        }

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if (count == items.length) {
                return false;
            } else {
                enqueue(object);
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    public void put(Object object) throws InterruptedException {
        if (object == null) {
            throw new NullPointerException("put object cannot is null");
        }

        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == items.length) { // 避免过早的通知
                notFull.await();
            }
            enqueue(object);
        } finally {
            lock.unlock();
        }

    }


    public Object poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if (count == 0) {
                return null;
            } else {
                return dequeue();
            }
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            return dequeue();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final ArrayBlockingQueue arrayQueue = new ArrayBlockingQueue(10);
        final AtomicInteger atomicInteger = new AtomicInteger();

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                arrayQueue.offer(atomicInteger.incrementAndGet());
            }).start();

            new Thread(() -> {
                System.out.println(arrayQueue.poll());
            }).start();
        }
    }
}
