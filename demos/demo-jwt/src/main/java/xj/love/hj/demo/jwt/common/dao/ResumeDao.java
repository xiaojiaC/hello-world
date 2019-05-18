package xj.love.hj.demo.jwt.common.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xj.love.hj.demo.jwt.common.po.ResumePo;

/**
 * 账户数据访问对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Repository
public interface ResumeDao extends JpaRepository<ResumePo, Long> {

    Optional<ResumePo> findByAccountId(long accountId);
}
