package xj.love.hj.demo.jmh;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * 基准测试样例
 * <pre>
 * 模式（Mode）：
 *     Throughput: 整体吞吐量，表示单位时间内可以执行多少次操作。
 *     AverageTime: 平均时间，表示每一次调用所需要的时间。
 *     SampleTime: 随机取样，最后输出取样结果的分布。例如：99%的调用在xx ms内，99.99%的调用在xx ms内。
 *     SingleShotTime: 测量单次操作的时间。往往伴随 warmup = 0，常用于测试冷启动时的性能。
 *     All: 内部JMH测试。
 *
 * 迭代（Iteration）：
 *     迭代时JMH的一次测量单位，在大部分测量模式下，一次迭代时长1s，表示1s内不间断的调用被测方法，并采样计算响应模式指标。
 *
 * 预热（Warmup）：
 *     由于Java虚拟机JIT的存在，同一个方法在JIT编译前后可能有所差异。通常只需考虑方法在JIT编译后的性能。
 *
 * 状态（State）：指定一个对象的作用范围。
 *     Thread: 一个对象只会被一个线程访问。多线程环境下，会为每个线程生成一个对象。
 *     Benchmark: 多个线程共享一个实例。
 *     Group: 在同一组中的所有线程之间共享所有相同类型的实例。每个线程组将提供自己的状态对象。
 * </pre>
 *
 * @author xiaojia
 * @see <a href="https://openjdk.java.net/projects/code-tools/jmh/">JMH</a>
 * @since 1.0
 */
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Timeout(time = 3)
public class Hello {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void measureAverageTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public void measureSampleTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    /**
     * <pre>
     * 如何运行此测试：
     *
     * 您应该看到大量迭代的运行，并且每次迭代伴随着非常大的吞吐量数字。
     *
     * a) 通过命令行：
     *     $ mvn clean install -DskipTests=true
     *     $ java -jar target/benchmarks.jar He
     * JMH生成自包含的JAR，将JMH与它捆绑在一起。
     * JMH的运行时可用选项“-h”：
     *     $ java -jar target/benchmarks.jar -h
     *
     * b) 通过Java API:
     *     从IDE运行时，请参阅JMH主页以了解可能的警告：http://openjdk.java.net/projects/code-tools/jmh/
     * </pre>
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Hello.class.getSimpleName()) // 指定测试类
                .forks(1) // 使用的进程个数
                .build();
        new Runner(opt).run();
    }
}
