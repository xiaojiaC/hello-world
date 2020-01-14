package xj.love.hj.demo.reactor;

import java.time.Duration;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import reactor.core.publisher.Flux;

/**
 * 拆分Flux
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.window">Splitting a
 * Flux</a>
 * @since 1.0
 */
public final class SplitFlux {

    private SplitFlux() {
    }

    public static void examples() {
        // 我想将一个 Flux<T> 拆分为一个 Flux<Flux<T>>
        Flux.range(1, 10)
                .window(3) // 以个数为界拆分
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        Flux.range(10, 10)
                .window(3, 2) // 以个数为界拆分，出现重叠的情况
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        Flux.range(20, 10)
                .window(3, 4) // 以个数为界拆分，出现丢弃的情况
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofMillis(100))
                .window(Duration.ofMillis(200)) // 以时间为界拆分
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        Flux.range(1, 10)
                .delayElements(Duration.ofMillis(300))
                .windowTimeout(5, Duration.ofMillis(2000)) // 以个数或时间为界
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        Flux.range(10, 10)
                .windowUntil(i -> i % 3 == 0, true) // 基于对元素的判断条件，满足条件的元素会分到下一波
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        Flux.range(20, 10)
                .windowWhile(i -> i % 3 == 0) // 基于对元素的判断条件，只要满足条件的元素
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        Flux.range(30, 10)
                .delayElements(Duration.ofMillis(100))
                .window(Flux.interval(Duration.ofMillis(200))) // 由另一个发布者发出信号为界
                .concatMap(Flux::collectList)
                .subscribe(System.out::println);

        // 我想将一个 Flux<T> 的元素拆分到集合
        Flux.range(1, 10)
                .buffer(3, HashSet::new) // 以个数为界拆分，且放到指定类型的容器
                .subscribe(System.out::println);

        // 我想将 Flux<T> 中具有共同特征的元素分组到子 Flux
        Flux.range(1, 10)
                .groupBy(i -> i % 2 == 0 ? "even" : "odd")
                .concatMap(g -> g.map(String::valueOf)
                        .startWith(g.key()))
                .subscribe(System.out::println);
    }

    public static void main(String[] args) throws InterruptedException {
        SplitFlux.examples();
        TimeUnit.SECONDS.sleep(3);
    }

}
