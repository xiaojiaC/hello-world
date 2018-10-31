package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.impl.ValidationServiceImpl;
import xj.love.hj.demo.dubbo.service.ValidationService;

/**
 * 验证服务供应商配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class ValidationProviderConfig {

    @Bean
    public ValidationService validationServiceImpl() {
        return new ValidationServiceImpl();
    }

    /**
     * 验证服务配置
     */
    @Bean
    public ServiceConfig validationServiceConfig(ApplicationConfig applicationConfig,
            RegistryConfig registryConfig,
            List<ProtocolConfig> protocolsConfig) {
        ServiceConfig<ValidationService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setProtocols(protocolsConfig);
        service.setInterface(ValidationService.class);
        service.setRef(validationServiceImpl());
        service.setGroup("validation");

        service.export();

        return service;
    }

}
