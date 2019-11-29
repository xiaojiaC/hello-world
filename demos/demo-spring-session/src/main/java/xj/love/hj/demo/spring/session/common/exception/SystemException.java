package xj.love.hj.demo.spring.session.common.exception;

/**
 * 系统异常
 *
 * @author xiaojia
 * @since 1.0
 */
public class SystemException extends BaseException {

    private static final long serialVersionUID = 4128442932524685913L;

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
