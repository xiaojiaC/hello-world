package xj.love.hj.demo.jwt.common.util;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xj.love.hj.demo.jwt.common.exception.SystemException;

/**
 * {@link KeyPair}生成器
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
public final class KeyPairGenerator {

    /**
     * RSA算法
     */
    public static final String ALGORITHM_RSA = "RSA";
    /**
     * 键长度2048
     */
    public static final int KEY_SIZE_2048 = 2048;
    /**
     * 键长度3072
     */
    public static final int KEY_SIZE_3072 = 3072;
    /**
     * 键长度4096
     */
    public static final int KEY_SIZE_4096 = 4096;

    private KeyPairGenerator() {
    }

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * 依据RSA算法生成签名key，默认2048位
     */
    public static KeyPair generateByRSA() {
        return generate(ALGORITHM_RSA, KEY_SIZE_2048);
    }

    /**
     * 生成签名key
     *
     * @param algorithm 算法
     */
    public static KeyPair generate(String algorithm, int keySize) {
        try {
            java.security.KeyPairGenerator keyPairGenerator = java.security.KeyPairGenerator
                    .getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
            keyPairGenerator.initialize(KEY_SIZE_2048, new SecureRandom());
            return keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new SystemException("Failed to generate key pair", e);
        }
    }
}
