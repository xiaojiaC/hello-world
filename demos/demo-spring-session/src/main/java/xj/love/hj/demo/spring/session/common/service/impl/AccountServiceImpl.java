package xj.love.hj.demo.spring.session.common.service.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xj.love.hj.demo.spring.session.common.constant.enums.ErrorCode;
import xj.love.hj.demo.spring.session.common.dao.AccountDao;
import xj.love.hj.demo.spring.session.common.dto.AccountDto;
import xj.love.hj.demo.spring.session.common.exception.AuthorizationFailureException;
import xj.love.hj.demo.spring.session.common.po.AccountPo;
import xj.love.hj.demo.spring.session.common.service.AccountService;
import xj.love.hj.demo.spring.session.common.util.BeanCopier;
import xj.love.hj.demo.spring.session.common.util.Sha256Util;

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

    @Override
    public AccountDto login(String username, String password) {
        AccountPo account = accountDao.findByUsername(username);
        if (account == null) {
            throw new AuthorizationFailureException(ErrorCode.ACCOUNT_AUTH_INFO_ERROR);
        }

        password = Sha256Util.encryptPasswordWithSalt(password, account.getSalt());

        return accountDao.findByUsernameAndPassword(username, password)
                .map(accountPo -> {
                    accountDao.setLastLoggedAt(username, new Date());
                    return BeanCopier.of(accountPo, new AccountDto()).apply().get();
                })
                .orElseThrow(() ->
                        new AuthorizationFailureException(ErrorCode.ACCOUNT_AUTH_INFO_ERROR));
    }
}
