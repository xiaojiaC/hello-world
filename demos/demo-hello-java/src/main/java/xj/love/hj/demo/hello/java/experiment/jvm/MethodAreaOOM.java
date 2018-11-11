package xj.love.hj.demo.hello.java.experiment.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法区(含常量池)内存溢出。
 *
 * java6 vm args: -XX:PermSize=10m -XX:MaxPermSize=10m
 *
 * @author xiaojia
 * @since 1.0
 */
public class MethodAreaOOM {

    /**
     * <pre>
     * java6:
     * - Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
     *
     * java8:
     *  - Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=10m; support was removed in 8.0
     *  - Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=10m; support was removed in 8.0
     *  - Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * </pre>
     */
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            list.add(String.valueOf(i).intern());
        }
    }
}
