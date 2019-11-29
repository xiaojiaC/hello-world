package xj.love.hj.demo.hello.java.experiment.jvm;

/**
 * 虚拟机栈内存溢出。
 *
 * vm args: -Xss228k
 *
 * @author xiaojia
 * @since 1.0
 */
public class JVMStackOOM {

    static int stackSize = 0;

    private static void call(int a, int b) {
        stackSize++;
        call(++a, ++b);
    }

    /**
     * 1389
     * java.lang.StackOverflowError
     */
    public static void main(String[] args) {
        try {
            call(1, 2);
        } catch (StackOverflowError error) {
            System.out.println(stackSize);
            error.printStackTrace();
        }
    }
}
