package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.impl.GenericServiceImpl;
import xj.love.hj.demo.dubbo.service.BarService;

/**
 * 实现泛化调用服务供应商配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class GenericProviderConfig {

    @Bean
    public GenericService genericServiceImpl() {
        return new GenericServiceImpl();
    }

    /**
     * 暴露泛化实现
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/generic-service.html">实现泛化实现</a>
     */
    @Bean
    public ServiceConfig genericServiceConfig(ApplicationConfig applicationConfig,
            RegistryConfig registryConfig,
            List<ProtocolConfig> protocolsConfig) {
        ServiceConfig<GenericService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setProtocols(protocolsConfig);

        service.setInterface(BarService.class);
        service.setRef(genericServiceImpl()); // 泛化bar服务实现

        service.export();

        return service;
    }

}
