package xj.love.hj.demo.jwt.common.context;

import java.util.HashMap;
import java.util.Map;

/**
 * API线程上下文
 *
 * @author xiaojia
 * @since 1.0
 */
public class ApiThreadContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXTS =
            new ThreadLocal<>();
    private static final String KEY_API_VISITOR = "apiVisitor";

    /**
     * 获取上下文内容
     */
    public static Map<String, Object> get() {
        return CONTEXTS.get();
    }

    /**
     * 清空上下文内容
     */
    public static void clear() {
        CONTEXTS.remove();
    }

    private static void setAttribute(String name, Object value) {
        Map<String, Object> ctx = CONTEXTS.get();
        if (ctx == null) {
            ctx = new HashMap<>();
            CONTEXTS.set(ctx);
        }
        ctx.put(name, value);
    }

    private static Object getAttribute(String name) {
        Map<String, Object> ctx = CONTEXTS.get();
        return ctx == null ? null : ctx.get(name);
    }

    /**
     * 设置api访问者
     */
    public static void putApiVisitor(ApiVisitor apiVisitor) {
        setAttribute(KEY_API_VISITOR, apiVisitor);
    }

    /**
     * 获取api访问者
     */
    public static ApiVisitor getApiVisitor() {
        return (ApiVisitor) getAttribute(KEY_API_VISITOR);
    }
}
