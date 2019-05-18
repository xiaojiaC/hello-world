package xj.love.hj.demo.jwt.common.interceptor;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xj.love.hj.demo.jwt.common.context.ApiThreadContext;
import xj.love.hj.demo.jwt.common.context.ApiVisitor;
import xj.love.hj.demo.jwt.common.exception.UnauthorizedException;
import xj.love.hj.demo.jwt.common.po.AccountPo;
import xj.love.hj.demo.jwt.common.service.AccountService;

import static xj.love.hj.demo.jwt.common.constant.ApiConstants.HEADER_NAME_AUTH;
import static xj.love.hj.demo.jwt.common.constant.ApiConstants.HEADER_VALUE_PREFIX_AUTH;

/**
 * 授权拦截器
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        final String authHeader = request.getHeader(HEADER_NAME_AUTH);
        if (authHeader != null && authHeader.startsWith(HEADER_VALUE_PREFIX_AUTH)) {
            String authToken = authHeader.substring(HEADER_VALUE_PREFIX_AUTH.length());
            Optional<AccountPo> account = accountService.validateToken(authToken);
            if (account.isPresent()) {
                ApiThreadContext.putApiVisitor(ApiVisitor.of(account.get()));
                return true;
            }
        }

        throw new UnauthorizedException("Invalid authorization");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o,
            Exception e) throws Exception {
        ApiThreadContext.clear();
    }
}
