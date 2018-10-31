package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.impl.MergeServiceImpl;
import xj.love.hj.demo.dubbo.impl.MergeServiceImpl2;
import xj.love.hj.demo.dubbo.service.MergeService;

/**
 * 分组聚合菜单服务供应商配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class MergeProviderConfig {

    @Bean
    public MergeService mergeServiceImpl() {
        return new MergeServiceImpl();
    }

    @Bean
    public MergeService mergeServiceImpl2() {
        return new MergeServiceImpl2();
    }

    /**
     * 暴露第一种合并菜单服务实现配置
     */
    @Bean
    public ServiceConfig mergeServiceConfig(ApplicationConfig applicationConfig,
            RegistryConfig registryConfig,
            List<ProtocolConfig> protocolsConfig) {
        ServiceConfig<MergeService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setProtocols(protocolsConfig);
        service.setInterface(MergeService.class);
        service.setRef(mergeServiceImpl());
        service.setVersion("1.0.0");
        service.setGroup("merge");

        service.export();

        return service;
    }

    /**
     * 暴露第二种合并菜单服务实现配置
     */
    @Bean
    public ServiceConfig mergeServiceConfig2(ApplicationConfig applicationConfig,
            RegistryConfig registryConfig,
            List<ProtocolConfig> protocolsConfig) {
        ServiceConfig<MergeService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setProtocols(protocolsConfig);
        service.setInterface(MergeService.class);
        service.setRef(mergeServiceImpl2());
        service.setVersion("1.0.0");
        service.setGroup("merge2");

        service.export();

        return service;
    }

}
