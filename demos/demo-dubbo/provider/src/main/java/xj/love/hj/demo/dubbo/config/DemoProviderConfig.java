package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.impl.DemoServiceImpl;
import xj.love.hj.demo.dubbo.impl.DemoServiceImpl2;
import xj.love.hj.demo.dubbo.service.DemoService;

/**
 * Demo服务供应商配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class DemoProviderConfig {

    private static final int PROTOCOL_PORT = 20880;
    private static final String PROTOCOL_THREAD_POOL = "fixed";
    private static final int PROTOCOL_THREAD_NUMBER = 200;

    /**
     * 服务实现
     */
    @Bean
    public DemoService demoServiceImpl() {
        return new DemoServiceImpl();
    }

    @Bean
    public DemoService demoServiceImpl2() {
        return new DemoServiceImpl2();
    }

    /**
     * 服务方法级配置
     */
    @Bean
    public List<MethodConfig> methodsConfig() {
        List<MethodConfig> methods = new ArrayList<MethodConfig>();

        /*
         * 配置覆盖关系: reference method > service method > reference > service > consumer > provider
         */
        MethodConfig method = new MethodConfig();
        method.setName("sayHello");
        // method.setTimeout(1000);
        method.setRetries(0);

        methods.add(method);
        return methods;
    }

    /**
     * 当前应用配置
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("demo-provider");
        return application;
    }

    /**
     * 连接注册中心配置
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/subscribe-only.html">只订阅</a>
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/registry-only.html">只注册</a>
     */
    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("redis");
        registry.setAddress("127.0.0.1:6379");
        // registry.setRegister(false); // 禁用注册。一个正在开发中的服务提供者注册，可能会影响消费者不能正常运行。通过直连测试正在开发的服务.
        // registry.setSubscribe(false); // 禁用订阅。注册中心还没来得及部署应用依赖的服务，此时只能注册，暂不提供订阅。
        return registry;
    }

    /**
     * 服务提供者协议配置
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/thread-model.html">线程模型</a>
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/multi-protocols.html">多协议</a>
     */
    @Bean
    public List<ProtocolConfig> protocolsConfig() {
        List<ProtocolConfig> protocols = new ArrayList<>();

        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(PROTOCOL_PORT);
        protocol.setDispatcher("message"); // 设置线程模型， 只有请求响应消息派发到线程池，其它连接断开事件，心跳等消息，直接在 IO 线程上执行
        protocol.setThreadpool(PROTOCOL_THREAD_POOL); // 设置线程池配置， 固定大小线程池，启动时建立线程，不关闭，一直持有。(缺省)
        protocol.setThreads(PROTOCOL_THREAD_NUMBER);
        protocols.add(protocol);

        // protocol = new ProtocolConfig();
        // protocol.setName("rmi");
        // protocol.setPort(1099);
        // protocols.add(protocol);

        return protocols;
    }

    /**
     * 服务提供者暴露服务配置
     *
     * 注意： ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口，请自行缓存，否则可能造成内存和连接泄漏
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/fault-tolerent-strategy.html">集群容错</a>
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/loadbalance.html">负载均衡</a>
     */
    @Bean
    public ServiceConfig serviceConfig() {
        ServiceConfig<DemoService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig());
        service.setRegistry(registryConfig()); // 多个注册中心可以用setRegistries()
        service.setProtocols(protocolsConfig()); // 多个协议可以用setProtocols()
        service.setInterface(DemoService.class);
        service.setRef(demoServiceImpl());
        service.setMethods(methodsConfig()); // 方法级设置
        service.setVersion("1.0.0");
        service.setGroup("demo");

        service.setCluster("failover"); // 设置集群容错模式， 失败自动切换
        service.setLoadbalance("roundrobin"); // 设置负载均衡策略， 轮循服务器

        service.export(); // 暴露及注册服务

        return service;
    }

    /**
     * 服务提供者暴露Bar服务第二种实现配置
     */
    @Bean
    public ServiceConfig serviceConfig2() {
        ServiceConfig<DemoService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig());
        service.setRegistry(registryConfig());
        service.setProtocols(protocolsConfig());
        service.setInterface(DemoService.class);
        service.setRef(demoServiceImpl2());
        service.setVersion("1.0.0");
        service.setGroup("demo2"); // 当一个接口有多种实现时，可以用 group 区分。

        service.export();

        return service;
    }

}
