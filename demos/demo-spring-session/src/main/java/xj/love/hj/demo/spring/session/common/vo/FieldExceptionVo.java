package xj.love.hj.demo.spring.session.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 通用字段异常错误
 *
 * @param <T> 字段原始值类型
 * @author xiaojia
 * @since 1.0
 */
@ApiModel(description = "字段异常信息")
public class FieldExceptionVo<T> {

    /**
     * 异常字段
     */
    @Getter
    @ApiModelProperty(value = "字段")
    private String field;

    /**
     * 字段异常信息
     */
    @Getter
    @ApiModelProperty(value = "字段异常信息", position = 1)
    private String message;

    /**
     * 原始值
     */
    @Getter
    @ApiModelProperty(value = "字段原始值", position = 2)
    private T value;

    private FieldExceptionVo(String field, String message) {
        this(field, message, null);
    }

    private FieldExceptionVo(String field, String message, T value) {
        this.field = field;
        this.message = message;
        this.value = value;
    }

    /**
     * 字段异常简述
     *
     * @param field 字段
     * @param message 异常消息
     * @return 字段异常错误信息
     */
    public static FieldExceptionVo brief(String field, String message) {
        return new FieldExceptionVo(field, message);
    }

    /**
     * 字段异常详情，包含异常字段原始值
     *
     * @param field 字段
     * @param message 异常消息
     * @param value 字段原始值
     * @param <V> 字段原始值类型
     * @return 字段异常错误信息
     */
    public static <V> FieldExceptionVo<V> detail(String field, String message, V value) {
        return new FieldExceptionVo<>(field, message, value);
    }
}
