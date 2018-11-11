package xj.love.hj.demo.hello.java.experiment.classloader;

/**
 * 字段解析。
 *
 * @author xiaojia
 * @since 1.0
 */
public class FieldResolution {

    /**
     * 接口0，包含A
     */
    interface Interface0 {

        int A = 0;
    }

    /**
     * 接口1，包含A
     */
    interface Interface1 extends Interface0 {

        int A = 1;
    }

    /**
     * 父类实现接口1，包含A
     */
    static class Parent implements Interface1 {

        public static final int A = 2;
    }

    /**
     * 子类实现接口1，包含A
     */
    static class Sub extends Parent implements Interface1 {

        // 把该行注释一下看看，编译器将拒绝编译
        public static final int A = 3;
    }

    public static void main(String[] args) {
        System.out.println(Sub.A);
    }
}
