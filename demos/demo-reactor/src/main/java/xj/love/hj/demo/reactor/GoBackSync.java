package xj.love.hj.demo.reactor;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 回到同步的世界
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.blocking">Going Back
 * to the Synchronous World</a>
 * @since 1.0
 */
public final class GoBackSync {

    private GoBackSync() {
    }

    public static void examples() {
        // 注意：如果从计划从标记为“仅非阻塞”（默认为parallel()和single()）的调度程序中调用，
        // 则除Mono#toFuture以外的所有这些方法都将引发UnsupportedOperatorException。
        Flux.interval(Duration.ofMillis(300))
                .publishOn(Schedulers.single())
                .map(v -> Mono.just(v).hide().blockOptional())
                .onErrorReturn(Optional.empty()) // 注释该行试试看
                .subscribe();

        // 我有一个 Flux<T>，我想
        long i = Flux.interval(Duration.ofMillis(300))
                .blockFirst(); // 在拿到第一个元素前阻塞
        // .blockLast(); // 在拿到最后一个元素前阻塞
        System.out.println(i);

        try {
            Flux.interval(Duration.ofMillis(300))
                    .blockFirst(Duration.ofMillis(200)); // 在拿到第一个元素前阻塞，并指定超时时限
        } catch (Exception e) {
            System.out.println(e.getClass() + " " + e.getMessage());
        }

        Iterable<Integer> it = Flux.range(1, 10)
                .toIterable(); // 同步地转换为Iterable<T>
        System.out.println(it);

        Stream<Integer> stream = Flux.range(1, 10)
                .toStream(); // 同步地转换为Stream<T>
        System.out.println(stream);

        // 我有一个 Mono<T>，我想
        i = Mono.delay(Duration.ofMillis(300))
                .block(); // 在拿到元素前阻塞
        System.out.println(i);

        try {
            Mono.delay(Duration.ofMillis(300))
                    .block(Duration.ofMillis(200)); // 在拿到元素前阻塞，并指定超时时限
        } catch (Exception e) {
            System.out.println(e.getClass() + " " + e.getMessage());
        }

        CompletableFuture<Long> future = Mono.delay(Duration.ofMillis(300))
                .toFuture(); // 转换为CompletableFuture<T>
        future.whenComplete((l, e) -> System.out.println(l + " " + e));
    }

    public static void main(String[] args) throws InterruptedException {
        GoBackSync.examples();
        TimeUnit.SECONDS.sleep(3);
    }
}
