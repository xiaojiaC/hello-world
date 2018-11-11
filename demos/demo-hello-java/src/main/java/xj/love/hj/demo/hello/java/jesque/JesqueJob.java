package xj.love.hj.demo.hello.java.jesque;

import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.Client;
import net.greghaines.jesque.client.ClientImpl;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 用于在Redis中存放Jesque Job详情。
 *
 * @author xiaojia
 * @since 1.0
 */
public class JesqueJob {

    private static Client client = new ClientImpl(JesqueConfig.getRedisConfig());

    public static void createJob(Job job) {
        enqueue(Queue.QUEUE.name(), job);
    }

    public static void createDelayJob(Job job, long future) {
        delayedEnqueue(Queue.DELAYQUEUE.name(), job, future);
    }

    private static void enqueue(String queue, Job job) {
        synchronized (client) {
            try {
                client.enqueue(queue, job);
            } catch (JedisConnectionException e) {
                client.enqueue(queue, job);
            }
        }
    }

    private static void delayedEnqueue(String queue, Job job, long future) {
        synchronized (client) {
            try {
                client.delayedEnqueue(queue, job, future);
            } catch (JedisConnectionException e) {
                client.delayedEnqueue(queue, job, future);
            }
        }
    }
}
