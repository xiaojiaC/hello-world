package xj.love.hj.demo.reactor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 过滤序列
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.filtering">Filtering a
 * Sequence</a>
 * @since 1.0
 */
public class FilterSequence {

    private FilterSequence() {
    }

    public static void examples() {
        // 我想过滤一个序列
        Flux.just(-1, 1, 1, "a", "b", 2, 1)
                .ofType(Integer.class) // 仅限于指定类型的对象
                .filter(i -> i > 0) // 基于给定的判断条件
                .distinctUntilChanged() // 去掉连续且重复的元素
                .distinct() // 去掉重复的元素
                .subscribe(System.out::println);

        // 我只想要一部分序列
        Flux.just("a", "b", "c", "d", "e")
                .take(4) // 只要4个元素
                .takeLast(3) // 只要最后3个元素
                .takeUntil(s -> s.equals("c")) // 发出元素直到满足某个条件为止
                .takeWhile(s -> s.equals("b")) // 满足某个条件时才发出元素
                .next() // 只取第一个元素
                .subscribe(System.out::println);

        Flux.range(1, 3)
                .takeUntilOther(Flux.never()) // 当给定发布者发出元素时，停止从此Flux获取值
                .subscribe(System.out::println);

        // 最多只取1个元素
        Flux.just("a", "b", "c", "d", "e")
                .elementAt(2) // 给定序号
                // .takeLast(1) // 最后一个
                // .last() // 如果为序列空则发出错误信号
                // .last("f") // 如果为序列空则发出默认值
                .subscribe(System.out::println);

        // 跳过一些元素
        Flux.just(1, 2, 3, 4, 5)
                // .skip(1) // 跳过1个元素
                .skip(Duration.ZERO) // 跳过一段时间内发出的元素
                .skipLast(1) // 跳过最后1个元素
                .skipUntil(i -> i == 2) // 跳过元素直到满足条件才开始发出
                .skipUntilOther(Flux.empty()) // 从此Flux跳过值，直到指定的发布者发出onNext或onComplete信号为止
                .skipWhile(i -> i == 2)  // 满足某个条件时才跳过元素
                .subscribe(System.out::println);

        // 采样
        Flux.range(1, Integer.MAX_VALUE)
                .delayElements(Duration.ofMillis(100))
                .sample(Duration.ofSeconds(1))
                .subscribe(System.out::println);
        Flux.range(1, 10)
                .delayElements(Duration.ofMillis(300))
                .sampleTimeout(d -> Mono.delay(Duration.ofMillis(100 * d)))
                .subscribe(System.out::println);

        // 我只想要一个元素
        Flux.just("single")
                .single() // 如果为空或多于一个就返回错误
                .subscribe(System.out::println);
        Flux.empty().single("singleDefaultValue") // 如果为空发出一个缺省值
                .subscribe(System.out::println);
        Flux.empty().singleOrEmpty() // 如果为空就返回一个空序列
                .subscribe();
    }

    public static void main(String[] args) throws InterruptedException {
        FilterSequence.examples();
        TimeUnit.SECONDS.sleep(3);
    }
}
