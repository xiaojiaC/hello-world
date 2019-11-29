package xj.love.hj.demo.hello.java.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志打印者，被用于测试log4j/logback的日志输出。
 *
 * @author xiaojia
 * @since 1.0
 */
public class LogPrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPrinter.class);

    public static void trace() {
        LOGGER.trace("trace level info");
    }

    public static void debug() {
        LOGGER.debug("debug level info");
    }

    public static void info() {
        LOGGER.info("info level info");
    }

    public static void warn() {
        LOGGER.warn("warn level info");
    }

    public static void error() {
        LOGGER.error("error level info");
    }
}
