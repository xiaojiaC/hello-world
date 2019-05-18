package xj.love.hj.demo.fine.uploader.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 响应
 *
 * @author xiaojia
 * @since 1.0
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private boolean success; // fine-uploader需要依此判断上传是否成功
    private int code;
    private String message;
    private T data;

    private Response(HttpStatus httpStatus) {
        if (HttpStatus.OK.equals(httpStatus)) {
            this.success = true;
        }
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    /**
     * 成功响应
     */
    public static Response success() {
        return new Response(HttpStatus.OK);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T> 响应数据类型
     */
    public static <T> Response<T> success(T data) {
        Response response = new Response(HttpStatus.OK);
        response.setData(data);
        return response;
    }

    /**
     * 失败响应
     */
    public static Response failed() {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 失败响应
     *
     * @param code 响应码
     * @param message 失败消息
     */
    public static Response failed(int code, String message) {
        Response response = new Response();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
