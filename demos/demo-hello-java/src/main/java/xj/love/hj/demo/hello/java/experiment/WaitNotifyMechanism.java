package xj.love.hj.demo.hello.java.experiment;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 等待通知机制，生产者消费者示例。
 *
 * @author xiaojia
 * @since 1.0
 */
public class WaitNotifyMechanism {

    private static final int CAPACITY = 10;

    /**
     * 生产者线程
     */
    private static class Producer extends Thread {

        private final Queue<Integer> warehouse;

        private Producer(Queue<Integer> warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (warehouse) {
                    while (warehouse.size() > CAPACITY) { // 使用while而不是if，是为了防止过早通知
                        try {
                            System.out.println("仓库已满...");
                            warehouse.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("生产中... ");
                    warehouse.add(0);
                    warehouse.notify();
                }
            }
        }
    }

    /**
     * 消费者线程
     */
    private static class Consumer extends Thread {

        private final Queue<Integer> warehouse;

        private Consumer(Queue<Integer> warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (warehouse) {
                    while (warehouse.isEmpty()) {
                        try {
                            System.out.println("仓库已空...");
                            warehouse.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("消费中... ");
                    warehouse.remove();
                    warehouse.notify();
                }
            }
        }
    }

    @SuppressWarnings("JdkObsolete")
    public static void main(String[] args) {
        // 共享资源
        final Queue<Integer> warehouse = new LinkedList<>();

        new Consumer(warehouse).start();
        new Producer(warehouse).start();
    }
}
