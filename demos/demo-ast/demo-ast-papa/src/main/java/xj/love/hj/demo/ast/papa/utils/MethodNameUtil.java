package xj.love.hj.demo.ast.papa.utils;

/**
 * 方法名工具类。
 *
 * @author xiaojia
 * @since 1.0
 */
public final class MethodNameUtil {

    private MethodNameUtil() {
    }

    public static String upperFirstChar(String name) {
        if (name.length() < 1) {
            return name;
        }
        String firstChar = name.substring(0, 1).toUpperCase();
        if (name.length() > 1) {
            return firstChar + name.substring(1);
        }
        return firstChar;
    }
}
