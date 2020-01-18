package xj.love.hj.demo.spring.session.common.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import xj.love.hj.demo.spring.session.common.vo.ResponseVo;

/**
 * API响应处理器
 *
 * @author xiaojia
 * @since 1.0
 */
@ControllerAdvice(annotations = RestController.class)
public class ApiResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (selectedContentType.includes(MediaType.APPLICATION_JSON)
                || selectedContentType.includes(MediaType.APPLICATION_JSON_UTF8)) {
            ServletServerHttpResponse resp = (ServletServerHttpResponse) response;
            if (HttpStatus.OK.value() == resp.getServletResponse().getStatus()) {
                return ResponseVo.success(body);
            }
            return body; // 异常场景统一在Handler中处理
        }
        return body;
    }
}
