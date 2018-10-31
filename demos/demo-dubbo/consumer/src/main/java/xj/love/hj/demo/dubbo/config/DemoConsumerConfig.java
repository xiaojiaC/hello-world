package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.service.DemoService;

/**
 * Demo服务消费者配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class DemoConsumerConfig {

    private static final int RETRY_TIMEOUT = 3000;
    private static final int NUMBER_OF_RETRIES = 3;

    /**
     * 服务方法级配置
     *
     * 配置覆盖关系: reference method > service method > reference > service > consumer > provider
     */
    @Bean
    public List<MethodConfig> methodsConfig() {
        List<MethodConfig> methods = new ArrayList<MethodConfig>();
        return methods;
    }

    /**
     * 当前应用配置
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("demo-consumer");
        return application;
    }

    /**
     * 连接注册中心配置
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/preflight-check.html">启动时检查</a>
     */
    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("redis");
        registry.setAddress("127.0.0.1:6379");
        // registry.setCheck(false); // 注册订阅失败时，也允许启动，需使用此选项，将在后台定时重试
        return registry;
    }

    /**
     * 引用远程服务配置
     *
     * 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接，请自行缓存，否则可能造成内存和连接泄漏
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/service-group.html">服务分组</a>
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/multi-versions.html">多版本</a>
     */
    @Bean
    public ReferenceConfig referenceConfig() {
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setId("demoService");
        reference.setApplication(applicationConfig());
        reference.setRegistry(registryConfig()); // 多个注册中心可以用setRegistries()
        reference.setInterface(DemoService.class);
        reference.setMethods(methodsConfig());

        /*
         * 版本迁移步骤:
         *
         * 1. 在低压力时间段，先升级一半提供者为新版本
         * 2. 再将所有消费者升级为新版本
         * 3. 然后将剩下的一半提供者升级为新版本
         */
        reference.setVersion("1.0.0"); // 多版本
        reference.setGroup("demo");

        /*
         * 配置优先级： JVM args > mapping files > reference url
         */
        // reference.setUrl("dubbo://127.0.0.1:20880"); // 设置直连提供者
        reference.setCheck(false); // 关闭服务的启动时检查 (没有提供者时报错)
        reference.setTimeout(RETRY_TIMEOUT);
        // 当出现失败，重试其它服务器,通常用于读操作，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)
        reference.setRetries(NUMBER_OF_RETRIES);
        return reference;
    }

}
