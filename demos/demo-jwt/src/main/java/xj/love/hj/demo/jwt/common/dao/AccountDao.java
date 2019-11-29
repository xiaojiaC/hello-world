package xj.love.hj.demo.jwt.common.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xj.love.hj.demo.jwt.common.po.AccountPo;

/**
 * 账户数据访问对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Repository
public interface AccountDao extends JpaRepository<AccountPo, Long> {

    Optional<AccountPo> findByUsername(String username);

    Optional<AccountPo> findByUsernameAndPassword(String username, String password);

}
