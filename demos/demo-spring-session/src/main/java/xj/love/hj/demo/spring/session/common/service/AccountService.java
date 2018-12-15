package xj.love.hj.demo.spring.session.common.service;

import xj.love.hj.demo.spring.session.common.dto.AccountDto;

/**
 * 账户服务
 *
 * @author xiaojia
 * @since 1.0
 */
public interface AccountService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录后的用户
     */
    AccountDto login(String username, String password);
}
