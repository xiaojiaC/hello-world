package xj.love.hj.demo.spring.session.common.constant.enums;

/**
 * 系统业务错误码定义
 *
 * @author xiaojia
 * @since 1.0
 */
public enum ErrorCode {

    /**
     * 账户: 授权失败
     */
    ACCOUNT_AUTH_FAILED(10000, "account.auth.failed"),
    /**
     * 账户: 授权信息填写错误
     */
    ACCOUNT_AUTH_INFO_ERROR(10001, "account.username.or.password.error");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
