package xj.love.hj.demo.spring.session.web.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xj.love.hj.demo.spring.session.common.annotation.NoLogin;
import xj.love.hj.demo.spring.session.common.constant.WebConstants;
import xj.love.hj.demo.spring.session.common.dto.AccountDto;
import xj.love.hj.demo.spring.session.common.service.AccountService;
import xj.love.hj.demo.spring.session.common.util.BeanCopier;
import xj.love.hj.demo.spring.session.web.account.form.LoginForm;
import xj.love.hj.demo.spring.session.web.account.vo.AccountVo;

/**
 * 登录控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@RestController
@RequestMapping(value = WebConstants.API_PREFIX + "/account",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "用户服务", tags = "User.Manage")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @NoLogin
    @PostMapping("login")
    @ApiOperation("用户登录")
    public AccountVo login(@Valid LoginForm form, HttpServletRequest request) {
        AccountDto accountDto = accountService.login(form.getUsername(), form.getPassword());
        request.getSession().setAttribute(WebConstants.SESSION_VISITOR, accountDto);
        return BeanCopier.of(accountDto, new AccountVo()).apply().get();
    }

    @GetMapping("logout")
    @ApiOperation("用户登出")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
