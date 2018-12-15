package xj.love.hj.demo.spring.session.common.util;

/**
 * 可转化的实体
 *
 * @param <S> 源实体类型
 * @param <T> 目标实体类型
 * @author xiaojia
 * @since 1.0
 */
@FunctionalInterface
public interface Convertible<S, T> {

    /**
     * 将转换实体{@code S}转换成{@code T}
     *
     * @param entity 源实体
     * @return 转换后的实体
     */
    T convert(S entity);
}
