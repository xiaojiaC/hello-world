package xj.love.hj.demo.hello.java.experiment.jvm;

/**
 * 本地方法栈内存溢出。
 *
 * java6 vm args: -Xss228k
 *
 * @author xiaojia
 * @since 1.0
 */
public class NativeMethodStackOOM {

    private void nonStop() {
        while (true) {

        }
    }

    private void newNonStopThreads() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    nonStop();
                }
            });
            thread.setName("test" + thread.getId());
            thread.start();
        }
    }

    public static void main(String[] args) {
        NativeMethodStackOOM oom = new NativeMethodStackOOM();
        oom.newNonStopThreads();
    }
}
