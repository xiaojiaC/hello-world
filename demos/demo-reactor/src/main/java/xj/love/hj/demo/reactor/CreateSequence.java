package xj.love.hj.demo.reactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 创建一个新序列
 *
 * @author xiaojia
 * @see <a href="https://projectreactor.io/docs/core/release/reference/#which.create">Creating a New
 * Sequence…​</a>
 * @since 1.0
 */
public class CreateSequence {

    private CreateSequence() {
    }

    public static void examples() throws Exception {
        Mono.just(1)
                .subscribe(System.out::println);

        String i = null;
        i = "justOrEmpty";
        Mono.justOrEmpty(i)
                .subscribe(System.out::println);

        Optional<String> io = Optional.empty();
        io = Optional.of("justOrEmpty");
        Mono.justOrEmpty(io)
                .switchIfEmpty(Mono.just("default"))
                .subscribe(System.out::println);

        Mono.fromSupplier(() -> "fromSupplier") // 懒创建
                .subscribe(System.out::println);

        Mono.defer(() -> Mono.just("defer")) // 懒创建
                .subscribe(System.out::println);

        Flux.just("a", "b")
                .subscribe(System.out::println);

        Flux.fromArray(new String[]{"fromArrayItem1", "fromArrayItem2"})
                .subscribe(System.out::println);

        Flux.fromIterable(Arrays.asList("fromIterable1", "fromIterable2"))
                .subscribe(System.out::println);

        Flux.range(2, 2)
                .subscribe(System.out::println);

        Flux.fromStream(Stream.of("fromStream1", "fromStream2"))
                .subscribe(System.out::println);

        Flux.from(Mono.just("from"))
                .subscribe(System.out::println);

        Mono.fromRunnable(() -> {
            System.out.println("fromRunnable");
        }).subscribe();

        Mono.fromCallable(() -> "fromCallable").subscribe(System.out::println);

        Mono.fromFuture(CompletableFuture.supplyAsync(() -> "from")
                .thenApply(s -> s + "Future")
        ).subscribe(System.out::println);

        Mono.empty() // 直接完成
                .switchIfEmpty(Mono.just("empty"))
                .subscribe(System.out::println);

        Mono.error(new RuntimeException()) // 立即生成错误
                .onErrorReturn("error")
                .subscribe(System.out::println);

        Mono.never().subscribe(System.out::println); // 什么都不做

        AtomicBoolean isDisposed = new AtomicBoolean();
        Disposable disposableInstance = new Disposable() {

            @Override
            public void dispose() {
                isDisposed.set(true);
            }

            @Override
            public String toString() {
                return "Disposable";
            }
        };
        Flux.using( // 依赖于可回收的资源
                () -> disposableInstance, // 生成资源
                disposable -> Flux.just("using" + disposable.toString()), // 处理资源，依据它发布元素
                Disposable::dispose // 完成时回收资源
        ).subscribe(System.out::println);

        Flux.generate( // 同步生成，可使用状态
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 2) {
                        sink.complete();
                    }
                    return state + 1; // 状态变更
                }).subscribe(System.out::println);

        MyEventProcessor myEventProcessor = new MyEventProcessor();
        Flux.create(sink -> { // 异步/同步生成，可用于桥接
            myEventProcessor.register(
                    new MyEventListener<String>() {

                        public void onDataChunk(List<String> chunk) {
                            for (String s : chunk) {
                                sink.next(s);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    });
        }).subscribe(System.out::println);
        Thread thread = new Thread(myEventProcessor);
        thread.start();
        thread.join();
    }

    static class MyEventProcessor implements Runnable {

        private List<MyEventListener<String>> listeners = new ArrayList<>();

        public void register(MyEventListener<String> listener) {
            this.listeners.add(listener);
        }

        @Override
        public void run() {
            listeners.stream().forEach(l -> {
                l.onDataChunk(Arrays.asList("create1", "create2"));
                l.processComplete();
            });
        }
    }

    interface MyEventListener<T> {

        void onDataChunk(List<T> chunk);

        void processComplete();
    }

    public static void main(String[] args) throws Exception {
        CreateSequence.examples();
    }
}
