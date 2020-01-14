package xj.love.hj.demo.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 窥视序列
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.peeking">Peeking into
 * a Sequence</a>
 * @since 1.0
 */
public class PeekSequence {

    private PeekSequence() {
    }

    public static void examples() {
        // 在不对序列造成改变的情况下

        // 我想得到通知或执行一些操作
        Flux.just("doOnNext1", "doOnNext2")
                .doOnNext(a -> System.out.println(a)) // 发出元素成功时
                .doOnComplete(() -> System.out.println("doOnComplete")) // 序列完成时
                .doOnSubscribe(s -> System.out.println("doOnSubscribe")) // 订阅时
                .doOnRequest(r -> System.out.println("doOnRequest:" + r)) // 请求时
                .doOnEach(s -> System.out.println(s)) // 发出元素成功或失败时
                .doOnTerminate(() -> System.out.println("doOnTerminate")) // 完成或错误终止时
                .doAfterTerminate(() -> System.out.println("doAfterTerminate")) // 终止信号向下游传递之后
                .doFinally(s -> System.out.println("doFinally")) // 所有结束的情况(完成，错误，取消)
                .subscribe();
        Mono.error(() -> new IllegalStateException())
                .doOnError(e -> System.out.println(e)) // 因错误终止时
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .subscribe();
        Mono.just(1)
                .doOnCancel(() -> System.out.println("doOnCancel")) // 取消时
                .doOnSubscribe(s -> s.cancel())
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .subscribe();

        // 记录日志
        Flux.just("log1", "log2")
                .log()
                .subscribe(System.out::println);

        // 我想知道所有的事件
        Flux.just("a", "b")
                .doOnEach( // single对象
                        s -> System.out.println(String.format("sign:%s , element: %s", s.getType(),
                                s.get()))
                )
                .subscribe();

        Flux.just("c", "d")
                .materialize() // 每个元素转换为single对象
                .subscribe(
                        s -> System.out.println(String.format("sign:%s , element: %s", s.getType(),
                                s.get())));

        Flux.just("e", "f")
                .materialize() // 转换为 single 对象
                .dematerialize() // 再转换回元素
                .subscribe(System.out::println);
    }

    public static void main(String[] args) {
        PeekSequence.examples();
    }
}
