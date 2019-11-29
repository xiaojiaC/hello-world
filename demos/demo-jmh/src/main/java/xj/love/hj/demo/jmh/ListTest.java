package xj.love.hj.demo.jmh;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

/**
 * CopyOnWriteArrayList vs ConcurrentLinkedQueue
 *
 * @author xiaojia
 * @since 1.0
 */
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Threads(4)
public class ListTest {

    private CopyOnWriteArrayList<Object> smallCopyOnWriteList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Object> bigCopyOnWriteList = new CopyOnWriteArrayList<>();
    private ConcurrentLinkedQueue<Object> smallConcurrentList = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Object> bigConcurrentList = new ConcurrentLinkedQueue<>();

    @Setup
    public void setup() {
        for (int i = 0; i < 10; i++) {
            smallCopyOnWriteList.add(new Object());
            smallConcurrentList.add(new Object());
        }
        for (int i = 0; i < 1000; i++) {
            bigCopyOnWriteList.add(new Object());
            bigConcurrentList.add(new Object());
        }
    }

    @Benchmark
    public void smallConcurrentListSize() {
        smallConcurrentList.size();
    }

    @Benchmark
    public void smallCopyOnWriteListSize() {
        smallCopyOnWriteList.size();
    }

    @Benchmark
    public void smallConcurrentListRead() {
        smallConcurrentList.peek();
    }

    @Benchmark
    public void smallCopyOnWriteListRead() {
        smallCopyOnWriteList.get(0);
    }

    @Benchmark
    public void smallConcurrentListWrite() {
        smallConcurrentList.add(new Object());
    }

    @Benchmark
    public void smallCopyOnWriteListWrite() {
        smallCopyOnWriteList.add(new Object());
    }

    @Benchmark
    public void bigConcurrentListRead() {
        bigConcurrentList.peek();
    }

    @Benchmark
    public void bigCopyOnWriteListRead() {
        bigCopyOnWriteList.get(0);
    }

    @Benchmark
    public void bigConcurrentListWrite() {
        bigConcurrentList.add(new Object());
    }

    @Benchmark
    public void bigCopyOnWriteListWrite() {
        bigCopyOnWriteList.add(new Object());
    }

}
