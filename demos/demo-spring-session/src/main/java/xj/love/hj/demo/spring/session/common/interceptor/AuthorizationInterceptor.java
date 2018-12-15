package xj.love.hj.demo.spring.session.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xj.love.hj.demo.spring.session.common.annotation.NoLogin;
import xj.love.hj.demo.spring.session.common.constant.WebConstants;
import xj.love.hj.demo.spring.session.common.constant.enums.ErrorCode;
import xj.love.hj.demo.spring.session.common.context.WebThreadContext;
import xj.love.hj.demo.spring.session.common.context.WebVisitor;
import xj.love.hj.demo.spring.session.common.dto.AccountDto;
import xj.love.hj.demo.spring.session.common.exception.AuthorizationFailureException;

/**
 * 用户授权拦截器
 *
 * @author xiaojia
 * @since 1.0
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (isNoNeedLogin(handlerMethod)) {
            return true;
        }

        AccountDto accountDto = (AccountDto) request.getSession()
                .getAttribute(WebConstants.SESSION_VISITOR);
        if (accountDto != null) {
            WebVisitor webVisitor = new WebVisitor();
            webVisitor.setAccountDto(accountDto);
            WebThreadContext.setVisitor(webVisitor);
            return true;
        }

        throw new AuthorizationFailureException(ErrorCode.ACCOUNT_AUTH_FAILED);
    }

    private boolean isNoNeedLogin(HandlerMethod handlerMethod) {
        NoLogin noLogin = handlerMethod.getMethodAnnotation(NoLogin.class);
        if (noLogin != null) {
            return true;
        }
        return false;
    }
}
