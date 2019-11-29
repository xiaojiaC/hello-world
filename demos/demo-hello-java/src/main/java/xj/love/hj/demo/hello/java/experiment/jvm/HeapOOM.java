package xj.love.hj.demo.hello.java.experiment.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆内存溢出。
 *
 * vm args: -Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author xiaojia
 * @since 1.0
 */
public class HeapOOM {

    /**
     * 内存溢出对象
     */
    private static class OOMObject {

    }

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     */
    public static void main(String[] args) {
        List<OOMObject> objs = new ArrayList<>();
        while (true) {
            objs.add(new OOMObject());
        }
    }
}
