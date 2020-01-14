package xj.love.hj.demo.reactor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 处理错误
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.errors">Handling
 * Errors</a>
 * @since 1.0
 */
public final class HandleError {

    private HandleError() {
    }

    public static void examples() {
        // 我想创建一个错误序列
        Flux.error(() -> new RuntimeException())
                .onErrorReturn("error") // 返回缺省值
                .subscribe(System.out::println);

        Flux.concat(Flux.error(() -> new RuntimeException())) // 任何错误都会立即中断序列并转发到下游
                .onErrorReturn("concat error")
                .subscribe(System.out::println);

        Flux.just("timeout")
                .delayElements(Duration.ofSeconds(1))
                .timeout(Duration.ofMillis(500)) // 如果元素超时未发出
                .doOnError(e -> System.out.println(e.getClass()))
                .subscribe();

        // 我想要类似 try/catch 的表达方式
        Flux.error(() -> new RuntimeException("onErrorResume"))
                .onErrorResume(e -> { // 返回一个Flux或Mono
                    if (e instanceof RuntimeException) {
                        System.out.println(e.getMessage());
                    }
                    return Flux.just("onErrorResume default");
                }).subscribe(System.out::println);

        Flux.error(() -> new RuntimeException("onErrorMap"))
                .onErrorReturn("onErrorMap default") // 返回缺省值
                .onErrorMap(e -> new IllegalStateException(e)) // 包装异常后再抛出
                .doFinally(s -> System.out.println("doFinally")) // finally代码块
                .subscribe(System.out::println); // try-with-resources写法：using工厂方法

        // 我想从错误中恢复
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) {
                        return "tick " + input;
                    }
                    throw new RuntimeException("boom");
                })
                .retry(1)
                .elapsed()
                .subscribe(System.out::println, System.err::println);

        Flux.<String>error(new IllegalArgumentException())
                .retryWhen(companion -> companion // 实现多次重试
                        .zipWith(Flux.range(1, 4),
                                (error, index) -> {
                                    if (index < 4) {
                                        return index;
                                    } else {
                                        throw Exceptions.propagate(error);
                                    }
                                })
                )
                .onErrorReturn("retryWhen")
                .subscribe(System.out::println);

        Flux.<String>error(new IllegalArgumentException())
                .retryWhen(companion -> companion // 实现多次重试+指数退避
                        .doOnNext(s -> System.out.println(s + " at " + LocalTime.now()))
                        .zipWith(Flux.range(1, 4), (error, index) -> {
                            if (index < 4) {
                                return index;
                            } else {
                                throw Exceptions.propagate(error);
                            }
                        })
                        .flatMap(index -> Mono.delay(Duration.ofMillis(index * 100)))
                        .doOnNext(s -> System.out.println("retried at " + LocalTime.now()))
                );
    }

    public static void main(String[] args) throws InterruptedException {
        HandleError.examples();
        TimeUnit.SECONDS.sleep(3);
    }
}
