package xj.love.hj.demo.spring.session.common.exception;

import xj.love.hj.demo.spring.session.common.constant.enums.ErrorCode;

/**
 * 用户授权失败异常
 *
 * @author xiaojia
 * @since 1.0
 */
public class AuthorizationFailureException extends BaseException {

    public AuthorizationFailureException(ErrorCode errorCode,
            Object... errorMessageParams) {
        super(errorCode.getCode(), errorCode.getMessage(), errorMessageParams);
    }

    public AuthorizationFailureException(ErrorCode errorCode, Throwable cause,
            Object... errorMessageParams) {
        super(errorCode.getCode(), errorCode.getMessage(), cause, errorMessageParams);
    }
}
