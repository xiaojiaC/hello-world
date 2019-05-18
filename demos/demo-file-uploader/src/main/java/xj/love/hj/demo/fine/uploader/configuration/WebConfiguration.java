package xj.love.hj.demo.fine.uploader.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xj.love.hj.demo.fine.uploader.constant.Constants;

/**
 * Web配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(Constants.API_PREFIX + "/**");
    }
}
