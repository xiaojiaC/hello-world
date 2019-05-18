package xj.love.hj.demo.jwt.common.service;

import java.util.Optional;
import xj.love.hj.demo.jwt.common.po.AccountPo;

/**
 * 账户服务
 *
 * @author xiaojia
 * @since 1.0
 */
public interface AccountService {

    /**
     * 生成api访问token
     *
     * @param username 用户名
     * @param password 密码
     * @return jwt token
     */
    String generateToken(String username, String password);

    /**
     * 检验apiToken
     *
     * @param token jwt token
     * @return 校验成功返回账户信息，否则返回空
     */
    Optional<AccountPo> validateToken(String token);

}
