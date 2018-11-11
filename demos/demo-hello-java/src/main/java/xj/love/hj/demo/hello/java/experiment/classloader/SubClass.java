package xj.love.hj.demo.hello.java.experiment.classloader;

/**
 * 子类。
 *
 * @author xiaojia
 * @since 1.0
 */
public class SubClass extends SuperClass {

    static {
        System.out.println("SubClass init!");
    }
}
