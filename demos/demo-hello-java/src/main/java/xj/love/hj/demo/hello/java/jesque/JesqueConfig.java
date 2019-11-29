package xj.love.hj.demo.hello.java.jesque;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * Jesque使用的Redis配置。
 *
 * @author xiaojia
 * @since 1.0
 */
public class JesqueConfig {

    private static Config redisConfig;

    static {
        InputStream inputStream = JesqueConfig.class.getResourceAsStream("/db.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String host = properties.getProperty("redis.host");
        String port = properties.getProperty("redis.port");
        String password = properties.getProperty("redis.password");
        String database = properties.getProperty("redis.database");
        ConfigBuilder configBuilder = new ConfigBuilder();
        if (!StringUtils.isEmpty(host)) {
            configBuilder.withHost(host);
        }
        if (!StringUtils.isEmpty(port)) {
            configBuilder.withPort(Integer.parseInt(port));
        }
        if (!StringUtils.isEmpty(password)) {
            configBuilder.withPassword(password);
        }
        if (!StringUtils.isEmpty(database) && StringUtils.isNumeric(database)) {
            configBuilder.withDatabase(Integer.parseInt(database));
        }
        setRedisConfig(configBuilder.build());
    }

    public static Config getRedisConfig() {
        return redisConfig;
    }

    public static void setRedisConfig(Config redisConfig) {
        JesqueConfig.redisConfig = redisConfig;
    }
}
