package xj.love.hj.demo.jwt.common.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置信息
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtProperties {

    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期时间，单位ms
     */
    private int expiration;

}
