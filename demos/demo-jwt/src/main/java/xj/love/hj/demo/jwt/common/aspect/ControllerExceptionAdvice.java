package xj.love.hj.demo.jwt.common.aspect;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import xj.love.hj.demo.jwt.common.constant.ApiConstants;
import xj.love.hj.demo.jwt.common.exception.SystemException;
import xj.love.hj.demo.jwt.common.exception.UnauthorizedException;

/**
 * 控制器异常切面
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
@ResponseBody
@ControllerAdvice(ApiConstants.BASE_PACKAGE)
public class ControllerExceptionAdvice {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleUnauthorizedException(UnauthorizedException e) {
        log.warn(e.getMessage(), e);
        return response(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleSystemException(SystemException e) {
        log.error(e.getMessage(), e);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private Map<String, Object> response(HttpStatus status, Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", status.value());
        result.put("msg", e.getMessage());
        return result;
    }
}
