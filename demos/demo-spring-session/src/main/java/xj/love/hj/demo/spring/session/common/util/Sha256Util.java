package xj.love.hj.demo.spring.session.common.util;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;
import xj.love.hj.demo.spring.session.common.constant.Constants;
import xj.love.hj.demo.spring.session.common.exception.SystemException;

/**
 * SHA256加密工具
 *
 * @author xiaojia
 * @since 1.0
 */
public final class Sha256Util {

    private Sha256Util() {
    }

    public static String encrypt(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digestBytes = messageDigest.digest(text.getBytes(Constants.UTF_8));
            return Hex.encodeHexString(digestBytes);
        } catch (Exception e) {
            throw new SystemException("Failed to using SHA encryption");
        }
    }

    public static String encryptPasswordWithSalt(String password, String salt) {
        return encrypt(encrypt(password) + salt);
    }
}
