package xj.love.hj.demo.hello.java.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import org.junit.Test;

public class FairAndUnfairTests {

    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2(false);

    @Test
    public void fair() throws Exception {
        testLock(fairLock);
    }

    @Test
    public void unfair() throws Exception {
        testLock(unfairLock);
    }

    private static void testLock(Lock lock) throws Exception {
        for (int i = 0; i < 5; i++) {
            Job job = new Job(lock);
            job.setName(String.valueOf(i));
            job.start();
        }
        Thread.sleep(3000);
    }

    private static class Job extends Thread {

        private Lock lock;

        private Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    Thread.sleep(100);
                    System.out.println(
                            "Lock by " + Thread.currentThread().getName() + ", Waiting by "
                                    + ((ReentrantLock2) lock).getQueuedThreads().stream()
                                    .map(Thread::getName).collect(Collectors.toList()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }

    }

    private static class ReentrantLock2 extends ReentrantLock {

        private ReentrantLock2(boolean fair) {
            super(fair);
        }

        //由于列表是逆序输出，为了方便观察结果，将其进行反转
        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<Thread>(super.
                    getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }
}
