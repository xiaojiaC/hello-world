package xj.love.hj.demo.spring.session.common.exception;

import xj.love.hj.demo.spring.session.common.constant.Constants;

/**
 * 基础异常抽象
 *
 * @author xiaojia
 * @since 1.0
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 7307963059257523804L;

    protected Integer errorCode;
    protected String errorMessage;
    protected Object[] errorMessageParams;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(Integer errorCode, String errorMessage, Object... errorMessageParams) {
        super(formatExceptionMessage(errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorMessageParams = errorMessageParams;
    }

    public BaseException(Integer errorCode, String errorMessage, Throwable cause,
            Object... errorMessageParams) {
        super(formatExceptionMessage(errorCode, errorMessage), cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorMessageParams = errorMessageParams;
    }

    private static String formatExceptionMessage(Integer errorCode, String errorMessage) {
        return String.format(Constants.EXCEPTION_MSG_FORMAT, errorCode, errorMessage);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object[] getErrorMessageParams() {
        return errorMessageParams;
    }
}
