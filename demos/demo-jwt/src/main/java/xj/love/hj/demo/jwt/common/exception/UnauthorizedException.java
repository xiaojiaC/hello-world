package xj.love.hj.demo.jwt.common.exception;

/**
 * 未授权异常
 *
 * @author xiaojia
 * @since 1.0
 */
public class UnauthorizedException extends SystemException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
