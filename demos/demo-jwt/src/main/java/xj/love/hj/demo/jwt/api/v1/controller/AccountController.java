package xj.love.hj.demo.jwt.api.v1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xj.love.hj.demo.jwt.api.v1.form.GetTokenForm;
import xj.love.hj.demo.jwt.api.v1.vo.AccountVo;
import xj.love.hj.demo.jwt.api.v1.vo.TokenVo;
import xj.love.hj.demo.jwt.common.constant.ApiConstants;
import xj.love.hj.demo.jwt.common.context.ApiThreadContext;
import xj.love.hj.demo.jwt.common.context.ApiVisitor;
import xj.love.hj.demo.jwt.common.po.AccountPo;
import xj.love.hj.demo.jwt.common.service.AccountService;

/**
 * 账户资源控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@RestController("v1AccountController")
@RequestMapping(value = ApiConstants.API_V1_PREFIX + "/account",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "账户服务", tags = "Account.Manage")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("token")
    @ApiOperation("获取账户token")
    public TokenVo getToken(GetTokenForm form) {
        String token = accountService.generateToken(form.getUsername(), form.getPassword());
        return new TokenVo(token);
    }

    @GetMapping
    @ApiOperation("获取账户信息")
    public AccountVo getAccount() {
        ApiVisitor apiVisitor = ApiThreadContext.getApiVisitor();
        AccountPo accountPo = apiVisitor.getAccountPo();
        AccountVo accountVo = new AccountVo();
        if (accountPo != null) {
            BeanUtils.copyProperties(accountPo, accountVo);
        }
        return accountVo;
    }
}
