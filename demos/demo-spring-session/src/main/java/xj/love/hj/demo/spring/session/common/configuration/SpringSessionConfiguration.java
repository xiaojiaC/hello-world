package xj.love.hj.demo.spring.session.common.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * Spring Session自定义配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
@EnableRedisHttpSession(
        maxInactiveIntervalInSeconds = 3600,
        redisNamespace = "demo:spring:session",
        redisFlushMode = RedisFlushMode.ON_SAVE
)
public class SpringSessionConfiguration {

    /**
     * 自定义Session序列化器，使用{@link Jackson2JsonRedisSerializer}替换默认的{@link
     * org.springframework.data.redis.serializer.JdkSerializationRedisSerializer}序列化
     */
    @Bean(name = "springSessionDefaultRedisSerializer")
    @SuppressWarnings("unchecked")
    public RedisSerializer<Object> redisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(
                Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    /**
     * X-Auth-Token授权头传递SessionId
     */
//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        return HeaderHttpSessionIdResolver.xAuthToken();
//    }

    /**
     * Cookie传递SessionId
     */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        CookieHttpSessionIdResolver sessionIdResolver = new CookieHttpSessionIdResolver();
        sessionIdResolver.setCookieSerializer(cookieSerializer());
        return sessionIdResolver;
    }

    /**
     * 自定义Cookie序列化器
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // 自定义cookie名为JSESSIONID，而不是默认的SESSION
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        /*
         * - 如果正则表达式不匹配，则不设置域，并且将使用现有域。
         * - 如果正则表达式匹配，则第一个分组将用作域。
         *
         * 这意味着对https://child.example.com的请求会将域设置为example.com。但是，对http://localhost:8080/或
         * http://192.168.1.100:8080/的请求将使cookie域保持未设置状态，因此仍然可以在开发中工作而无需进行任何生产更改。
         */
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        serializer.setUseHttpOnlyCookie(true);
        return serializer;
    }
}
