package xj.love.hj.demo.jwt.common.exception;

/**
 * 系统异常
 *
 * @author xiaojia
 * @since 1.0
 */
public class SystemException extends RuntimeException {

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

}
