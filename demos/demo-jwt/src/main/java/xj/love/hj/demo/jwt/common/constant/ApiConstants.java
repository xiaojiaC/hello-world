package xj.love.hj.demo.jwt.common.constant;

/**
 * Web API常量
 *
 * @author xiaojia
 * @since 1.0
 */
public final class ApiConstants {

    private ApiConstants() {
    }

    /**
     * 基础包名
     */
    public static final String BASE_PACKAGE = "xj.love.hj.demo.jwt";

    /**
     * API端点前缀
     */
    public static final String API_PREFIX = "/api";

    /**
     * API端点前缀
     */
    public static final String API_V1_PREFIX = API_PREFIX + "/v1";

    /**
     * 授权请求头，格式如：Authorization: Bearer <token>
     */
    public static final String HEADER_NAME_AUTH = "Authorization";
    /**
     * 授权请求头值前缀
     */
    public static final String HEADER_VALUE_PREFIX_AUTH = "Bearer ";

}
