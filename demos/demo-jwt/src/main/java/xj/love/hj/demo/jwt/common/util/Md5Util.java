package xj.love.hj.demo.jwt.common.util;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.digest.Md5Crypt;

/**
 * MD5工具类
 *
 * @author xiaojia
 * @see <a href="https://www.ibm.com/developerworks/cn/linux/l-md5crypt/index.html">md5crypt</a>
 * @since 1.0
 */
public final class Md5Util {

    public static final String PREFIX_PWD = "demo-jwt";

    private Md5Util() {
    }

    /**
     * 使用md5Crypt加密字符串。
     *
     * @param text 待加密文本
     * @return 密文
     */
    public static String encrypt(String text) {
        return Md5Crypt.md5Crypt(addPrefix(text).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 使用md5Crypt加密字符串，并与原密码进行比对。
     *
     * @param text 待加密文本
     * @param pwd 原密码，格式如：$1$[salt]$[encrypted-string]
     * @return 匹配返回true，否则返回false
     */
    public static boolean compare(String text, String pwd) {
        // md5Crypt会自动从原密码提取8位盐值
        String encrypted = Md5Crypt.md5Crypt(addPrefix(text).getBytes(StandardCharsets.UTF_8), pwd);
        return encrypted.equals(pwd);
    }

    private static String addPrefix(String key) {
        return PREFIX_PWD + key;
    }

    public static void main(String[] args) {
        String defaultPassword = encrypt("abc123");
        System.out.println(defaultPassword);
        System.out.println(compare("abc123", defaultPassword));
    }
}
