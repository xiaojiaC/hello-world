package xj.love.hj.demo.hello.java.jesque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jesque Job中将被测试执行的具体方法。
 *
 * @author xiaojia
 * @since 1.0
 */
public class TestMethod {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMethod.class);

    public void test(String str) {
        LOGGER.info("TestMethod.test() {}", new Object[]{str});
    }
}
