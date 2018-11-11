package xj.love.hj.demo.hello.java.experiment;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Exchanger: 交换者。用于线程间协作的工具类，用于进行线程间的数据交换。它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据。
 *
 * - 可以用于遗传算法，也可以用于校对工作。
 */
public class ExchangerTests {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @Test
    public void testDataValidation() {
        Exchanger<Integer> exchanger = new Exchanger<>();

        executor.execute(() -> {
            try {
                Integer countByA = 1002; // A清点的钞票数量
                exchanger.exchange(countByA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                Integer countByB = 1005; // B清点的钞票数量
                Integer countByA = exchanger.exchange(countByB);

                assertNotEquals(countByA, countByB); // A,B清点钞票数是否一致
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }
}
