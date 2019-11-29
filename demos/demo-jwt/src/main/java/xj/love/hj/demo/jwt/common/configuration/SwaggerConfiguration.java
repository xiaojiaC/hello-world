package xj.love.hj.demo.jwt.common.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import xj.love.hj.demo.jwt.common.constant.ApiConstants;

/**
 * Swagger API文档配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Environment environment;

    @Bean
    public Docket createApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(ApiConstants.API_PREFIX)
                .apiInfo(apiInfo(null))
                .select()
                .apis(RequestHandlerSelectors.basePackage(ApiConstants.BASE_PACKAGE + ".api.v0"))
                .paths(PathSelectors.any())
                .build();
        // .globalOperationParameters(globalOperationParams());
    }

    private ApiInfo apiInfo(String version) {
        return new ApiInfoBuilder()
                .title("api")
                .description("demo jwt api doc")
                .version("1.0")
                .build();
    }

    private List<Parameter> globalOperationParams() {
        List<Parameter> parameters = new ArrayList<>();
        Parameter accountIdReqHeaderParam = new ParameterBuilder()
                .name("api-account-id")
                .description("账户id")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();
        parameters.add(accountIdReqHeaderParam);
        return parameters;
    }

    @Bean
    public Docket createV1OfApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(ApiConstants.API_V1_PREFIX)
                .apiInfo(apiInfo("1.0"))
                .select()
                .apis(RequestHandlerSelectors.basePackage(ApiConstants.BASE_PACKAGE + ".api.v1"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    private ApiKey apiKey() {
        return new ApiKey("token", ApiConstants.HEADER_NAME_AUTH, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^.+(?<!token)$"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return Arrays.asList(new SecurityReference("token", authorizationScopes));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        int port = environment.getRequiredProperty("server.port", Integer.class);
        log.info("swagger: http://localhost:" + port + "/swagger-ui.html");
    }
}
