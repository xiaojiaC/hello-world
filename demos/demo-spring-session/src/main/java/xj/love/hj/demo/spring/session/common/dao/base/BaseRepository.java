package xj.love.hj.demo.spring.session.common.dao.base;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础仓库
 *
 * @param <T> 实体类型
 * @param <IdT> 实体id类型
 * @author xiaojia
 * @since 1.0
 */
@NoRepositoryBean
public interface BaseRepository<T, IdT extends Serializable> extends
        JpaRepository<T, IdT>, JpaSpecificationExecutor<T> {

}
