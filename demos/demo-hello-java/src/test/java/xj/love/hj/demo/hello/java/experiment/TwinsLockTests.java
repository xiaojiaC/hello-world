package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.locks.Lock;
import org.junit.Test;

public class TwinsLockTests {

    @Test
    public void test() {
        final Lock lock = new TwinsLock();

        class Worker extends Thread {

            @Override
            public void run() {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        }

        for (int i = 0; i < 6; i++) {
            Worker w = new Worker();
            w.setName(String.valueOf(i));
            w.start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
