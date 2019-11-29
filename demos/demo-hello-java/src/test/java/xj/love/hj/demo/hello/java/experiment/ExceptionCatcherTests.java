package xj.love.hj.demo.hello.java.experiment;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class ExceptionCatcherTests {

    private int threadNumber;
    private int expectedCatchExceptionNumber;

    public ExceptionCatcherTests(int threadNumber, int expectedCatchExceptionNumber) {
        this.threadNumber = threadNumber;
        this.expectedCatchExceptionNumber = expectedCatchExceptionNumber;
    }

    @Parameters
    public static Collection<Integer[]> getParams() {
        return Arrays.asList(new Integer[][]{
                {10, 10},
                {33, 33},
                {66, 66},
        });
    }

    @Test
    public void testCountCaughtSubThreadExceptions() throws Exception {
        int actualCatchExceptionNumber = ExceptionCatcher
                .countCaughtSubThreadExceptions(threadNumber);
        assertEquals(expectedCatchExceptionNumber, actualCatchExceptionNumber);
    }

    @Test
    public void testCountCaughtPoolThreadExceptions() throws Exception {
        int actualCatchExceptionNumber = ExceptionCatcher
                .countCaughtPoolThreadExceptions(threadNumber);
        assertEquals(expectedCatchExceptionNumber, actualCatchExceptionNumber);
    }
}
