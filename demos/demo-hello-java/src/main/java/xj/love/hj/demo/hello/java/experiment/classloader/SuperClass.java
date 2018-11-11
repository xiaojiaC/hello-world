package xj.love.hj.demo.hello.java.experiment.classloader;

/**
 * 超类。
 *
 * @author xiaojia
 * @since 1.0
 */
public class SuperClass {

    /**
     * 对于静态字段，只有直接定义这个字段的类才会被初始化，
     * 因此通过子类来引用父类中定义的静态字段，只会触发父类的初始化
     */
    public static int value = 111;

    static {
        System.out.println("SuperClass init!");
    }
}
