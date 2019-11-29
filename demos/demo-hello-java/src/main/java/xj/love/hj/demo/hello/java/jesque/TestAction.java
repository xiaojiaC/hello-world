package xj.love.hj.demo.hello.java.jesque;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于测试执行过程中传递给任务解决者的参数。
 *
 * @author xiaojia
 * @since 1.0
 */
public class TestAction implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestAction.class);

    private final Integer intVar;
    private final Double doubleVar;
    private final Boolean boolVar;
    private final String strVar;
    private final List<Object> listVar;

    public TestAction(final Integer intVar, final Double doubleVar, final Boolean boolVar,
            final String strVar, final List<Object> listVar) {
        this.intVar = intVar;
        this.doubleVar = doubleVar;
        this.boolVar = boolVar;
        this.strVar = strVar;
        this.listVar = listVar;
    }

    @Override
    public void run() {
        LOGGER.info("TestAction.run() {} {} {} {} {}",
                this.intVar, this.doubleVar, this.boolVar, this.strVar, this.listVar);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
    }
}
