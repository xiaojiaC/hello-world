package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.service.BarService;

/**
 * 泛化调用Bar服务消费者配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class GenericConsumerConfig {

    /**
     * 泛化接口调用方式主要用于客户端没有 API 接口及模型类元的情况，参数及返回值中的所有 POJO 均用 Map 表示，通常用于框架集成， 比如：实现一个通用的服务测试框架，可通过
     * GenericService 调用所有服务实现。
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/generic-reference.html">实现泛化调用</a>
     */
    @Bean
    public ReferenceConfig genericReferenceConfig(ApplicationConfig applicationConfig,
            RegistryConfig registryConfig) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setId("barService");
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);

        reference.setInterface(BarService.class);
        reference.setGeneric("true"); // 使用泛化调用bar服务

        return reference;
    }

}
