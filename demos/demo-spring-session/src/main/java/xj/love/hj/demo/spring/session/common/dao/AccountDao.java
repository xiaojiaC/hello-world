package xj.love.hj.demo.spring.session.common.dao;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xj.love.hj.demo.spring.session.common.dao.base.BaseRepository;
import xj.love.hj.demo.spring.session.common.po.AccountPo;

/**
 * 账户数据访问对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Repository
@Transactional
public interface AccountDao extends BaseRepository<AccountPo, Long> {

    AccountPo findByUsername(String username);

    Optional<AccountPo> findByUsernameAndPassword(String username, String password);

    @Modifying
    @Query("update AccountPo a set a.lastLoggedAt = :lastLoggedAt where a.username = :username")
    int setLastLoggedAt(@Param("username") String username,
            @Param("lastLoggedAt") Date loginTime);
}
