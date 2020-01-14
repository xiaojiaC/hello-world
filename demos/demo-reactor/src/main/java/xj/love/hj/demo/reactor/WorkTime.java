package xj.love.hj.demo.reactor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 基于时间的操作
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.time">Working with
 * Time</a>
 * @since 1.0
 */
public final class WorkTime {

    private WorkTime() {
    }

    public static void examples() {
        // 我想将元素转换为带有时间信息的 Tuple2<Long, T>
        Flux.range(1, 2)
                .delayElements(Duration.ofMillis(200))
                .elapsed() // 记录耗时
                .subscribe(System.out::println);

        Flux.range(1, 2)
                .delayElements(Duration.ofMillis(200))
                .timestamp() // 记录时间戳
                .subscribe(System.out::println);

        // 如果元素间延迟过长则中止序列
        Flux.just("a")
                .delayElements(Duration.ofSeconds(3))
                .timeout(Duration.ofMillis(300))
                .onErrorReturn("timeout")
                .subscribe(System.out::println);

        // 以固定的周期发出元素
        Flux.interval(Duration.ofMillis(200)) // 从0开始
                .subscribe(System.out::println);

        // 在一个给定的延迟后发出0
        Mono.delay(Duration.ofMillis(200))
                .subscribe(System.out::println);

        // 我想引入延迟
        Mono.just("delayElement")
                .delayElement(Duration.ofMillis(300)) // 延迟发出元素
                .subscribe(System.out::println);

        Mono.just("delaySubscription")
                .delaySubscription(Duration.ofMillis(300)) // 延迟订阅
                .subscribe(System.out::println);
    }

    public static void main(String[] args) throws InterruptedException {
        WorkTime.examples();
        TimeUnit.SECONDS.sleep(3);
    }
}
