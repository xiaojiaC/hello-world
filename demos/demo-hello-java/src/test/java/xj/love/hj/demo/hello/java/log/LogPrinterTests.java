package xj.love.hj.demo.hello.java.log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.assertTrue;

public class LogPrinterTests {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void testError() throws Exception {
        LogPrinter.error();

        assertTrue(systemOutRule.getLog().contains("error"));
    }

    @Test
    public void testInfo() throws Exception {
        LogPrinter.info();

        assertTrue(systemOutRule.getLog().contains("info"));
    }
}
