package xj.love.hj.demo.jvmti.agent.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

/**
 * 执行耗时拦截器
 *
 * @author xiaojia
 * @since 1.0
 */
public class TimingInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method,
            @Argument(0) HttpServletRequest request,
            @Argument(1) HttpServletResponse response,
            @SuperCall Callable<?> callable) {
        System.out.println("TimingInterceptor: " + method + " params "
                + getLogOfRequestParams(request));

        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } catch (Exception e) {
            System.err.println("TimingInterceptor: " + method + " call exception, "
                    + e.getMessage());
            return null;
        } finally {
            System.out.println("TimingInterceptor: " + method + " took "
                    + (System.currentTimeMillis() - start) + " ms");
        }
    }

    private static String getLogOfRequestParams(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("{");
        request.getParameterMap().forEach((k, v) ->
                builder.append(String.format("%s:%s", k,
                        v != null && v.length > 1 ? Arrays.toString(v) : v[0]))
        );
        return builder.append("}").toString();
    }
}
