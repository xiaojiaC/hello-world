package ${package}.${artifactId}.common.configuration;

import ${package}.${artifactId}.common.constant.Constants;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 国际化消息资源文件配置。
 *
 * {@code JSR 303 validation API}的提示信息默认配置在{@code ValidationMessages.properties}，这里将消息配置统一到{@code
 * Spring messages.properties}。
 *
 * @author xiaojia
 * @since 1.0
 * @see <a href="https://github.com/spring-projects/spring-boot/issues/3071">spring boot unable to
 * interpolate message for jsr-303 validation</a>
 */
@Configuration
public class MessagesConfiguration {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(Constants.UTF_8);
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
