package xj.love.hj.demo.jwt.common.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xj.love.hj.demo.jwt.common.dao.AccountDao;
import xj.love.hj.demo.jwt.common.exception.SystemException;
import xj.love.hj.demo.jwt.common.exception.UnauthorizedException;
import xj.love.hj.demo.jwt.common.po.AccountPo;
import xj.love.hj.demo.jwt.common.service.AccountService;
import xj.love.hj.demo.jwt.common.util.JwtTokenUtil;
import xj.love.hj.demo.jwt.common.util.Md5Util;

/**
 * 账户服务实现
 *
 * @author xiaojia
 * @since 1.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String generateToken(String username, String password) {
        Optional<AccountPo> account = accountDao.findByUsername(username);
        if (!account.isPresent()) {
            throw new SystemException("Username or password is incorrect");
        }

        String passwordInDb = account.get().getPassword();
        if (!Md5Util.compare(password, passwordInDb)) {
            throw new SystemException("Username or password is incorrect");
        }

        return jwtTokenUtil.generateToken(account.get());
    }

    @Override
    public Optional<AccountPo> validateToken(String token) {
        try {
            boolean isValidToken = jwtTokenUtil.validateToken(token);
            if (!isValidToken) {
                return Optional.empty();
            }
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("The token is expired");
        } catch (JwtException e) {
            throw new UnauthorizedException("The token is invalid");
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        return accountDao.findByUsername(username);
    }
}
