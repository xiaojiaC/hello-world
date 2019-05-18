package xj.love.hj.demo.jwt.common.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xj.love.hj.demo.jwt.common.constant.ApiConstants;
import xj.love.hj.demo.jwt.common.interceptor.AuthorizationInterceptor;
import xj.love.hj.demo.jwt.common.interceptor.OptionsPreFlightInterceptor;

/**
 * Web配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private OptionsPreFlightInterceptor optionsPreFlightInterceptor;
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(optionsPreFlightInterceptor)
                .addPathPatterns(ApiConstants.API_V1_PREFIX + "/**");
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns(ApiConstants.API_V1_PREFIX + "/**")
                .excludePathPatterns(ApiConstants.API_V1_PREFIX + "/account/token");
    }
}
