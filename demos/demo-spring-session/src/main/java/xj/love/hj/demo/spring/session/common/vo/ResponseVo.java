package xj.love.hj.demo.spring.session.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * 通用响应VO
 *
 * @param <T> 业务数据类型
 * @author xiaojia
 * @since 1.0
 */
@ApiModel(description = "响应体")
public class ResponseVo<T> {

    /**
     * 错误码: 为0表示无错误，否则为具体业务错误码
     */
    @Getter
    @ApiModelProperty(value = "错误码, 0表示无错误")
    private int code;

    /**
     * 错误消息: 错误码不为0时必有
     */
    @Getter
    @ApiModelProperty(value = "错误消息", position = 1)
    private String message;

    /**
     * 错误内容: 字段异常详情信息
     */
    @Getter
    @ApiModelProperty(value = "错误内容", position = 2)
    private List<FieldExceptionVo> fieldErrors;

    /**
     * 业务数据: 错误码为0时，返回的业务数据
     */
    @Getter
    @ApiModelProperty(value = "业务数据", position = 3)
    private T data;

    private ResponseVo(T data) {
        this.message = "success";
        this.data = data;
    }

    private ResponseVo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ResponseVo(int code, String message, List<FieldExceptionVo> fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static <V> ResponseVo<V> success(V data) {
        return new ResponseVo<>(data);
    }

    public static ResponseVo failure(int code, String message) {
        Assert.isTrue(code > 0, "Illegal error code!");
        return new ResponseVo<>(code, message);
    }

    public static ResponseVo failureWithFieldErrors(int code, String message,
            List<FieldExceptionVo> fieldErrors) {
        Assert.isTrue(code > 0, "Illegal error code!");
        return new ResponseVo<>(code, message, fieldErrors);
    }
}
