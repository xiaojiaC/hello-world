package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.service.ValidationService;

/**
 * 验证服务消费者配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class ValidationConsumerConfig {

    /**
     * 参数验证配置
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/parameter-validation.html">参数验证</a>
     */
    @Bean
    public ReferenceConfig validationReferenceConfig(ApplicationConfig applicationConfig,
            RegistryConfig registryConfig) {
        ReferenceConfig<ValidationService> reference = new ReferenceConfig<>();
        reference.setId("validationService");
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        reference.setInterface(ValidationService.class);
        reference.setGroup("validation");
        reference.setValidation("true");
        return reference;
    }

}
