package xj.love.hj.demo.hello.java.experiment.classloader;

/**
 * 常量类初始化。
 *
 * @author xiaojia
 * @since 1.0
 */
public class ConstClass {

    /**
     * 定义了常量
     */
    public static final String TEST = "test";

    static {
        System.out.println("ConstClass init!");
    }
}
