package xj.love.hj.demo.hello.java.jesque;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.MapBasedJobFactory;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerEvent;
import net.greghaines.jesque.worker.WorkerImpl;
import net.greghaines.jesque.worker.WorkerListener;

import static net.greghaines.jesque.utils.JesqueUtils.entry;
import static net.greghaines.jesque.utils.JesqueUtils.map;

/**
 * 工作者用于测试Jesque任务执行。
 *
 * @author xiaojia
 * @since 1.0
 */
public class Work {

    public static void main(String[] args) {
//        Job job = new Job("TestAction", 1, 2.3, true, "test", Arrays.asList("inner", 4.5));
//        JesqueJob.createJob(job);
//
//        final long delay = 3;
//        final long future = System.currentTimeMillis() + (delay * 1000); //unit: ms
//        JesqueJob.createDelayJob(job, future);
//
//        final Worker worker = new WorkerImpl(JesqueConfig.getRedisConfig(),
//                Collections.singletonList(Queue.DELAYQUEUE.name()),
//                new MapBasedJobFactory(map(entry("TestAction", TestAction.class))));

        String className = "xj.love.hj.demo.hello.java.jesque.TestMethod";
        String methodName = "test";
        List<String> methodParamTypes = new ArrayList<String>();
        methodParamTypes.add("java.lang.String");
        List<String> methodArgs = new ArrayList<String>();
        methodArgs.add("hello jesque!");

        Job job = new Job("Resolver", className, methodName, methodParamTypes, methodArgs);
        JesqueJob.createJob(job);

        final Worker worker = new WorkerImpl(JesqueConfig.getRedisConfig(),
                Collections.singletonList(Queue.QUEUE.name()),
                new MapBasedJobFactory(map(entry("Resolver", Resolver.class))));

        worker.getWorkerEventEmitter().addListener(new WorkerListener() {
            @Override
            public void onEvent(WorkerEvent event, Worker worker, String queue, Job job,
                    Object runner, Object result, Throwable t) {
                if (runner instanceof Resolver) {
                    System.out.println(runner.toString());
                }
            }
        }, WorkerEvent.JOB_EXECUTE);

        final Thread workerThread = new Thread(worker);
        workerThread.start();

        try {
            // Thread.sleep((delay * 1000) + 5000);
            Thread.sleep(5000);
        } catch (Exception e) {
            // ignore
        }
        worker.end(true);
        try {
            workerThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
