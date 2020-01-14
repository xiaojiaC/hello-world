package xj.love.hj.demo.reactor;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 对序列进行转换
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.values">Transforming
 * an Existing Sequence</a>
 * @since 1.0
 */
public class TransformSequence {

    private TransformSequence() {
    }

    public static void examples() {
        // 1对1地转换
        Flux.just("hello", "map")
                .map(String::length) // 转换
                .subscribe(System.out::println);

        Mono.just((CharSequence) "cast")
                .cast(String.class) // 类型转换
                .subscribe(s -> System.out.println(s.getClass()));

        Flux.just("index1", "index2")
                .index() // 为了获得每个元素的序号
                .subscribe(System.out::println);

        // 1对n地转换
        Function<String, String[]> doRequestFunc = s -> { // 假设是一个异步任务，根据uri异步拉取用户标签
            if ("uri1".equals(s)) {
                return new String[]{"book", "music"};
            }
            return new String[]{"default"};
        };
        Flux.fromIterable(Arrays.asList("uri1", "uri2"))
                .flatMap(s -> Flux.fromArray(doRequestFunc.apply(s))) // 对每一个元素执行一个异步操作，不保证顺序
                .subscribe(System.out::println);

        Flux.fromIterable(Arrays.asList("uri1", "uri2"))
                .flatMapSequential(s -> Flux
                        .fromArray(doRequestFunc.apply(s))) // 对每个元素的异步任务会立即执行，但会将结果按照原序列顺序排序
                .subscribe(System.out::println);

        Mono.just("uri1")
                .flatMapMany(s -> Flux.fromArray(doRequestFunc.apply(s))) // 当Mono的异步任务会返回多个元素的序列时
                .subscribe(System.out::println);

        // 1对n地转换，可自定义转化方法和/或状态
        Function<Integer, String> alphabetFunc = letterNumber -> {
            if (letterNumber < 1 || letterNumber > 26) {
                return null;
            }
            int letterIndexAscii = 'A' + letterNumber - 1;
            return "" + (char) letterIndexAscii;
        };
        Flux.just(-1, 30, 13, 9, 20)
                .handle((i, sink) -> {
                    String letter = alphabetFunc.apply(i);
                    if (letter != null) {
                        sink.next(letter);
                    }
                }).subscribe(System.out::println);

        // 添加一些数据元素到一个现有序列
        Flux.just("c")
                .startWith("b") // 在头部添加
                .startWith(Mono.just("a"))
                .concatWithValues("d") // 在尾部添加
                .concatWith(Flux.just("e", "f"))
                .subscribe(System.out::println);

        // 将Flux转换为集合
        Flux.just("a", "b")
                // .count() // 计数
                // .collectList() // 转换为 List
                // .collectMap(s -> s, String::length) // 转换为 Map
                // .reduce((s1, s2) -> s1 + " " + s2)
                .scan((s1, s2) -> s1 + " " + s2) // ​将每次reduce的结果立即发出
                .subscribe(System.out::println);

        // 将Flux转换为 Boolean
        Flux.just("a", "b")
                // .all(s -> !s.isEmpty()) // 对所有元素判断都为true
                // .any(s -> s.length() > 1) // 对至少一个元素判断为true
                // .hasElements() // 序列是否有元素
                .hasElement("c") // 序列是否有匹配的元素
                .subscribe(System.out::println);

        // 按序连接
        Flux.just(Flux.just(1, 2).concatWith(Flux.error(new Exception("test"))), Flux.just(3, 4))
                .concatMapDelayError(f -> f) // 依次订阅源来实现串联。过程中发生错误不会中断主序列，但是会在其余源有机会被合并之后传播
                .onErrorReturn(-1)
                .subscribe(System.out::println);
        Flux.mergeSequential(
                Flux.just(Flux.just("mergeSequential1"), Flux.just("mergeSequential2")))
                .subscribe(System.out::println);

        // 按元素发出的顺序合并
        Flux.merge(Mono.just("merge1"), Mono.just("merge2")) // 无论哪个序列的，元素先到先合并
                .subscribe(System.out::println);

        // 将元素组合
        Mono.zip(Mono.just("zip1"), Mono.just("zip2")) // 2个Mono组成1个Tuple2
                .subscribe(System.out::println);

        // 在终止信号出现时“采取行动”
        Mono.when(Mono.just("1"), Mono.just("2")).subscribe();

        // 选择第一个Mono发出的任何信号
        Mono.first(Mono.empty(), Mono.just(2))
                .defaultIfEmpty(0)
                .subscribe(System.out::println);

        // 以一定的间隔重复一个序列
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) {
                        return "tick " + input;
                    }
                    throw new RuntimeException("boom");
                })
                .onErrorReturn("Uh oh")
                .subscribe(System.out::println);

        // 空序列，缺省值
        Mono.empty().defaultIfEmpty("defaultIfEmpty").subscribe(System.out::println);
        Mono.empty().switchIfEmpty(Mono.just("switchIfEmpty")).subscribe(System.out::println);

        // 对序列的元素值不感兴趣
        int c[] = {0};
        Flux.range(1, 1000)
                .flatMap(v -> Mono.fromRunnable(() -> c[0]++))
                .ignoreElements() // 对序列的元素值不感兴趣
                .thenReturn("Result:")// 在序列结束之后返回一个值
                .subscribe(System.out::println);
        System.out.println(c[0]);

        // Mono延迟完成
        Mono.just("delayUntil")
                .delayUntil(a -> Flux.just(1, 2, 3)
                        .hide()
                        .delayElements(Duration.ofMillis(500)))
                .subscribe(System.out::println);
    }

    public static void main(String[] args) throws InterruptedException {
        TransformSequence.examples();
        TimeUnit.SECONDS.sleep(3);
    }
}
