package xj.love.hj.demo.hello.java.experiment;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 父线程捕获子线程异常的示例。
 *
 * @author xiaojia
 * @since 1.0
 */
public class ExceptionCatcher {

    /**
     * 任务示例，待捕获该任务抛出的异常
     */
    private static class Task implements Runnable {

        @Override
        public void run() {
            throw new NullPointerException();
        }
    }

    /**
     * UncaughtExceptionHandler: 当线程因未捕获的异常而突然终止时回调的处理程序接口。
     */
    private static class UncaughtExceptionHandlerImpl implements UncaughtExceptionHandler {

        private Map<Thread, Throwable> exceptions = new ConcurrentHashMap<>();
        private CountDownLatch countDownLatch;

        UncaughtExceptionHandlerImpl(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
//            System.out.println(
//                    String.format("[%s] throw exception[%s]", t.getName(), e));
            exceptions.putIfAbsent(t, e);
            countDownLatch.countDown();
        }

        public Map<Thread, Throwable> getExceptions() {
            return exceptions;
        }

    }

    /**
     * 自定义线程池中的线程配置。
     */
    private static class HandleThreadFactory implements ThreadFactory {

        private UncaughtExceptionHandler uncaughtExceptionHandler;

        HandleThreadFactory(UncaughtExceptionHandler handler) {
            this.uncaughtExceptionHandler = handler;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable); // 把任务对象放入线程中
//            // 线程池中的线程由于发生未预期异常而结束时，线程池会补充一个新的线程，因此你会看到2倍的线程正在被创建
//            System.out.println(thread.getName() + " is created");
            thread.setName("CustomThreadPool-" + thread.getId());
            thread.setUncaughtExceptionHandler(this.uncaughtExceptionHandler);
            return thread;
        }

    }

    public static int countCaughtSubThreadExceptions(int threadNum) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        UncaughtExceptionHandlerImpl handler = new UncaughtExceptionHandlerImpl(countDownLatch);
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(new Task());
            thread.setUncaughtExceptionHandler(handler);
            thread.start();
        }

        countDownLatch.await();

        // 捕获到的所有子线程的异常
        return handler.getExceptions().size();
    }

    public static int countCaughtPoolThreadExceptions(int threadNum) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        UncaughtExceptionHandlerImpl handler = new UncaughtExceptionHandlerImpl(countDownLatch);
        // Thread.setDefaultUncaughtExceptionHandler(handler);
        ExecutorService executor = Executors
                .newFixedThreadPool(threadNum, new HandleThreadFactory(handler));
        for (int i = 0; i < threadNum; i++) {
            executor.execute(new Task());
        }

        countDownLatch.await();
        executor.shutdown();

        return handler.getExceptions().size();
    }
}
