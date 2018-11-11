package xj.love.hj.demo.hello.java.experiment.jvm;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * 本机直接内存溢出。
 *
 * vm args: -Xmx10m -XX:MaxDirectMemorySize=2m
 *
 * @author xiaojia
 * @since 1.0
 */
public class DirectMemoryOOM {

    private static final int MB_1 = 1024 * 1024;

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError
     */
    public static void main(String[] args) throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(MB_1);
        }
    }
}
