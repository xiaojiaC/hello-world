package xj.love.hj.demo.spring.session.common.handler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import xj.love.hj.demo.spring.session.common.exception.AuthorizationFailureException;
import xj.love.hj.demo.spring.session.common.exception.BaseException;
import xj.love.hj.demo.spring.session.common.vo.FieldExceptionVo;
import xj.love.hj.demo.spring.session.common.vo.ResponseVo;

/**
 * 系统异常处理器
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class WebExceptionHandler /* extends ResponseEntityExceptionHandler */ {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVo handleBindException(BindException e) {
        return buildResponseVoWithFieldErrors(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(AuthorizationFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseVo handleAuthFailureException(AuthorizationFailureException e) {
        return buildResponseVo(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVo handleBaseException(Exception e) {
        return buildResponseVo(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseVo buildResponseVo(HttpStatus httpStatus, Exception exception) {
        log.error(exception.getMessage(), exception);
        int errorCode = httpStatus.value();
        String errorMessage = exception.getMessage();
        if (exception instanceof BaseException) {
            BaseException e = (BaseException) exception;
            errorCode = e.getErrorCode() != null ? e.getErrorCode() : errorCode;
            errorMessage = StringUtils.isNotBlank(e.getErrorMessage())
                    ? messageSource.getMessage(e.getErrorMessage(), e.getErrorMessageParams(),
                    Locale.getDefault())
                    : e.getMessage();
        }
        return ResponseVo.failure(errorCode, errorMessage);
    }

    private ResponseVo buildResponseVoWithFieldErrors(HttpStatus httpStatus,
            BindException exception) {
        log.error(exception.getMessage(), exception);
        int errorCode = httpStatus.value();
        String errorMessage = exception.getMessage();
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult.hasErrors()) {
            errorMessage = httpStatus.getReasonPhrase();
            List<FieldExceptionVo> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> {
                        String errMessage = messageSource.getMessage(fieldError.getDefaultMessage(),
                                fieldError.getArguments(), Locale.getDefault());
                        return FieldExceptionVo.brief(fieldError.getField(), errMessage);
                        // TODO: 定义 @Desensitization 用于字段脱敏，被标识的字段不会返回原始值
                        // return FieldExceptionVo.detail(fieldError.getField(), errMessage,
                        //      fieldError.getRejectedValue());
                    }).collect(Collectors.toList());
            return ResponseVo.failureWithFieldErrors(errorCode, errorMessage, fieldErrors);
        }
        return ResponseVo.failure(errorCode, errorMessage);
    }
}

