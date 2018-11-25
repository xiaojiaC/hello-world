package xj.love.hj.demo.spring.session.common.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import xj.love.hj.demo.spring.session.common.vo.FieldExceptionVo;
import xj.love.hj.demo.spring.session.common.vo.ResponseVo;

/**
 * Swagger文档配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error"))) // 忽略BasicErrorController中的错误路径
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages())
                .globalResponseMessage(RequestMethod.PUT, responseMessages())
                .globalResponseMessage(RequestMethod.DELETE, responseMessages())
                .additionalModels(typeResolver.resolve(FieldExceptionVo.class),
                        typeResolver.resolve(ResponseVo.class));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Demo Web APIs")
                .description("Spring Session Demo-用户登录/登出演示")
                .contact(new Contact("xiaojiaC", "", "xiaojia1100@163.com"))
                .version("1.0.0-SNAPSHOT")
                .build();
    }

    private List<ResponseMessage> responseMessages() {
        return Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.OK.value())
                        .message("Success")
                        .responseModel(new ModelRef("ResponseVo"))
                        .build(),
                responseMessage(HttpStatus.BAD_REQUEST),
                responseMessage(HttpStatus.UNAUTHORIZED),
                responseMessage(HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    private ResponseMessage responseMessage(HttpStatus httpStatus) {
        return new ResponseMessageBuilder().code(httpStatus.value())
                .message(httpStatus.getReasonPhrase())
                .responseModel(new ModelRef("ResponseVo"))
                .build();
    }
}
