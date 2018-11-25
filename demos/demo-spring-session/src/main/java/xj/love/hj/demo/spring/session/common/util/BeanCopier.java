package xj.love.hj.demo.spring.session.common.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.springframework.beans.BeanUtils;

/**
 * 实体属性值复制器。
 *
 * @param <S> 源实体类型
 * @param <T> 目标实体类型
 * @author xiaojia
 * @since 1.0
 */
public class BeanCopier<S, T> {

    private S source;
    private T target;

    private BeanCopier(S source, T target) {
        this.source = source;
        this.target = target;
    }

    /**
     * 获取一个实体拷贝器，用于源和目标实体不为null的场景。
     *
     * @param source 源实体
     * @param target 目标实体
     * @param <S> 源实体类型
     * @param <T> 目标实体类型
     * @return S->T 实体拷贝器
     */
    public static <S, T> BeanCopier<S, T> of(S source, T target) {
        Objects.requireNonNull(source, "Source can't is null");
        Objects.requireNonNull(source, "Target can't is null");

        return new BeanCopier(source, target);
    }

    /**
     * 获取一个实体拷贝器，用于源可能为null的场景。
     *
     * @param source 源实体
     * @param targetClass 目标实体类字节码
     * @param <S> 源实体类型
     * @param <T> 目标实体类型
     * @return S->T 实体拷贝器
     */
    public static <S, T> BeanCopier<S, T> ofNullable(S source, Class<T> targetClass) {
        BiFunction<S, T, BeanCopier> beanCopier = BeanCopier::new;
        return Optional.ofNullable(source).map(s -> {
            try {
                return beanCopier.apply(s, targetClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                return beanCopier.apply(s, null);
            }
        }).orElseGet(() -> beanCopier.apply(null, null));
    }

    /**
     * 应用默认的{@link BeanUtils#copyProperties(Object, Object)}拷贝函数。
     *
     * @return S->T 实体拷贝器
     */
    public BeanCopier<S, T> apply() {
        return apply(BeanUtils::copyProperties);
    }

    /**
     * 指定要应用的自定义拷贝函数。
     *
     * @param biConsumer bean属性复制二元消费者
     * @return S->T 实体拷贝器
     */
    public BeanCopier<S, T> apply(BiConsumer<S, T> biConsumer) {
        Optional.ofNullable(source).ifPresent(s -> biConsumer.accept(s, target));
        return this;
    }

    /**
     * 获取目标实体。
     *
     * @return 如果源实体为null，则返回null。否则返回目标实体
     */
    public T get() {
        return Optional.ofNullable(source).map(s -> target).orElse(null);
    }
}
